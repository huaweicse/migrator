package com.huaweicse.tools.migrator;

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

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Component
public class ModifyPomAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPomAction.class);

  public static final String FILE_SEPARATOR = File.separator;

  public static final String BASE_PATH = System.getProperty("user.dir");

  private List<File> pomFileList = new ArrayList<>();

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

  private void modifyContent() {
    pomFileList.forEach(file -> {
      try {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String pomJsonPath = BASE_PATH + FILE_SEPARATOR + "templates" + FILE_SEPARATOR + "pom.json";
        String pomJsonString = IOUtils.toString(new FileInputStream(pomJsonPath), StandardCharsets.UTF_8);
        Properties mavenProperties = model.getProperties();
        JSONObject pomJsonObject = JSONObject.parseObject(pomJsonString);
        Object jsonPomProperties = pomJsonObject.get("properties");
        if (!ObjectUtils.isEmpty(jsonPomProperties)) {
          JSONArray jsonPropertiesArrays = JSONArray.parseArray(jsonPomProperties.toString());
          jsonPropertiesArrays.forEach(property -> {
            JSONObject propertyJsonObject = JSONObject.parseObject(property.toString());
            JSONArray dataArrays = JSONArray.parseArray(propertyJsonObject.get("data").toString());
            String sign = propertyJsonObject.get("sign").toString();
            if ("add".equals(sign)) {
              dataArrays
                  .forEach(data -> mavenProperties.putIfAbsent(symbolValue(data, "label"), symbolValue(data, "value")));
            }
            if ("delete".equals(sign)) {
              dataArrays.forEach(data -> mavenProperties.remove(symbolValue(data, "artifactId")));
            }
          });
        }
        Object jsonPomDependencyManagements = pomJsonObject.get("dependencyManagement.dependencies");
        List<Dependency> managementDependencies = model.getDependencyManagement().getDependencies();
        if (!ObjectUtils.isEmpty(jsonPomDependencyManagements)) {
          JSONArray jsonDependencyManagementArrays = JSONArray.parseArray(jsonPomDependencyManagements.toString());
          jsonDependencyManagementArrays.forEach(management -> {
            JSONObject managementJsonObject = JSONObject.parseObject(management.toString());
            JSONArray dataArrays = JSONArray.parseArray(managementJsonObject.get("data").toString());
            String sign = managementJsonObject.get("sign").toString();
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
        if (!ObjectUtils.isEmpty(jsonPomDependencies)) {
          JSONArray jsonDependencyArrays = JSONArray.parseArray(jsonPomDependencies.toString());
          jsonDependencyArrays.forEach(dependencyData -> {
            JSONObject dependencyJsonObject = JSONObject.parseObject(dependencyData.toString());
            JSONArray dataArrays = JSONArray.parseArray(dependencyJsonObject.get("data").toString());
            String sign = dependencyJsonObject.get("sign").toString();
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
