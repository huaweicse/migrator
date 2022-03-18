package com.huaweicse.tools.migrator;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class ModifyPomAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPomAction.class);

  public static final String FILE_SEPARATOR = File.separator;

  public static final String BASE_PATH = System.getProperty("user.dir");

  private List<File> pomFileList = new ArrayList<>();

  public String frameType;

  public String getFrameType() {
    return frameType;
  }

  @Override
  public void run(String... args) {
    File folder = new File(args[0]);
    File[] files = folder.listFiles();
    if (files == null) {
      return;
    }
    Stream.of(files).forEach(this::filterPomFile);
    modifyContent();
  }

  private void filterPomFile(File file) {
    if ("pom.xml".equals(file.getName())) {
      pomFileList.add(file);
    } else {
      File child = new File(file, "pom.xml");
      if (child.exists()) {
        pomFileList.add(child);
      }
    }
  }

  protected void modifyContent() {
    pomFileList.forEach(file -> {
      try {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String pomJsonPath = BASE_PATH + FILE_SEPARATOR + "templates" + FILE_SEPARATOR + getFrameType() + ".pom.json";
        String pomJsonString = IOUtils.toString(new FileInputStream(pomJsonPath), StandardCharsets.UTF_8);
        JSONObject pomJsonObject = JSONObject.parseObject(pomJsonString);
        Object jsonPomProperties = pomJsonObject.get("properties");
        Properties mavenProperties = model.getProperties();
        if (!ObjectUtils.isEmpty(jsonPomProperties) && !ObjectUtils.isEmpty(mavenProperties)) {
          JSONArray jsonPropertiesArrays = JSONArray.parseArray(jsonPomProperties.toString());
          jsonPropertiesArrays.forEach(property -> {
            JSONObject propertyJsonObject = JSONObject.parseObject(property.toString());
            JSONArray dataArrays = JSONArray.parseArray(propertyJsonObject.get("data").toString());
            String sign = propertyJsonObject.get("sign").toString();
            if ("add".equals(sign)) {
              dataArrays.forEach(data -> mavenProperties.putIfAbsent(
                  symbolValue(data, "label"), symbolValue(data, "value")));
            }
            if ("delete".equals(sign)) {
              dataArrays.forEach(data -> mavenProperties.remove(symbolValue(data, "artifactId")));
            }
          });
          model.setProperties(new OrderedWriteProperties(mavenProperties));
        }
        Object jsonPomDependencyManagements = pomJsonObject.get("dependencyManagement.dependencies");
        DependencyManagement dependencyManagement = model.getDependencyManagement();
        if (!ObjectUtils.isEmpty(jsonPomDependencyManagements) && !ObjectUtils.isEmpty(dependencyManagement)) {
          List<Dependency> managementDependencies = dependencyManagement.getDependencies();
          JSONArray jsonDependencyManagementArrays = JSONArray.parseArray(jsonPomDependencyManagements.toString());
          jsonDependencyManagementArrays.forEach(management -> {
            JSONObject managementJsonObject = JSONObject.parseObject(management.toString());
            String sign = managementJsonObject.get("sign").toString();
            JSONArray dataArrays = JSONArray.parseArray(managementJsonObject.get("data").toString());
            if ("add".equals(sign)) {
              dataArrays.forEach(data -> {
                Dependency dependency = genDependency(symbolValue(data, "groupId"),
                    symbolValue(data, "artifactId"),
                    symbolValue(data, "version"),
                    symbolValue(data, "type"),
                    symbolValue(data, "scope"));
                managementDependencies.add(dependency);
              });
            }
            if ("delete".equals(sign)) {
              dataArrays.forEach(data -> managementDependencies.removeIf(
                  dependency -> ((JSONObject) data).get("artifactId").toString().equals(dependency.getArtifactId())));
            }
          });
        }
        Object jsonPomDependencies = pomJsonObject.get("dependencies");
        List<Dependency> dependencies = model.getDependencies();
        if (!ObjectUtils.isEmpty(jsonPomDependencies) && !ObjectUtils.isEmpty(dependencies)) {
          JSONArray jsonDependencyArrays = JSONArray.parseArray(jsonPomDependencies.toString());
          jsonDependencyArrays.forEach(dependencyData -> {
            JSONObject dependencyJsonObject = JSONObject.parseObject(dependencyData.toString());
            String sign = dependencyJsonObject.get("sign").toString();
            JSONArray dataArrays = JSONArray.parseArray(dependencyJsonObject.get("data").toString());
            if ("add".equals(sign)) {
              dataArrays.forEach(data -> {
                Dependency dependency = genDependency(symbolValue(data, "groupId"),
                    symbolValue(data, "artifactId"),
                    symbolValue(data, "version"),
                    null, null);
                dependencies.add(dependency);
              });
            }
            if ("delete".equals(sign)) {
              dataArrays.forEach(data -> dependencies.removeIf(
                  dependency -> ((JSONObject) data).get("artifactId").toString().equals(dependency.getArtifactId())));
            }
          });
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),
            StandardCharsets.UTF_8);
        new MavenXpp3Writer().write(outputStreamWriter, model);
        IOUtils.close(outputStreamWriter);

        // 重新写一遍，修改换行
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        for (String line : lines) {
          tempStream.write(line);
          tempStream.write(LINE_SEPARATOR);
        }
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (Exception ex) {
        LOGGER.error("Process pom.xml [{}] failed", file.getAbsolutePath(), ex);
      }
    });
  }

  private String symbolValue(Object data, String symbol) {
    return ((JSONObject) data).get(symbol).toString();
  }

  private Dependency genDependency(String groupId, String artifactId, String version, String type, String scope) {
    Dependency dependency = new Dependency();
    dependency.setGroupId(groupId);
    dependency.setArtifactId(artifactId);
    if (!ObjectUtils.isEmpty(version)) {
      dependency.setVersion(version);
    }
    if (!ObjectUtils.isEmpty(type)) {
      dependency.setType(type);
    }
    if (!ObjectUtils.isEmpty(type)) {
      dependency.setScope(scope);
    }
    return dependency;
  }
}
