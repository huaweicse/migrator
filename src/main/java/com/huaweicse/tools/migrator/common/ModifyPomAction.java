package com.huaweicse.tools.migrator.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class ModifyPomAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPomAction.class);

  public static final String BASE_PATH = System.getProperty("user.dir");

  private List<File> pomFileList = new ArrayList<>();

  public abstract String getFrameType();

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
        //properties
        processProperties(model, pomJsonObject);
        //dependencyManagement.dependencies
        processDependencyManagement(model, pomJsonObject);
        //dependencies
        processDependencies(model, pomJsonObject);
        //build -> plugins
        processPlugins(model, pomJsonObject);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),
            StandardCharsets.UTF_8);
        new MavenXpp3Writer().write(outputStreamWriter, model);
        IOUtils.close(outputStreamWriter);
      } catch (Exception ex) {
        LOGGER.error("Process pom.xml [{}] failed", file.getAbsolutePath(), ex);
      }
    });
  }

  private void processPlugins(Model model, JSONObject pomJsonObject) {
    Object pluginsObj = pomJsonObject.get("build.plugins");
    if (pluginsObj != null) {
      Build build = model.getBuild();
      if (!ObjectUtils.isEmpty(build)) {
        List<Plugin> plugins = build.getPlugins();
        JSONArray jsonPluginArrays = JSONArray.parseArray(pluginsObj.toString());
        jsonPluginArrays.forEach(pluginData -> {
          JSONObject dependencyJsonObject = JSONObject.parseObject(pluginData.toString());
          String sign = dependencyJsonObject.get("operation").toString();
          JSONArray dataArrays = JSONArray.parseArray(dependencyJsonObject.get("data").toString());
          if ("add".equals(sign)) {
            dataArrays.forEach(data -> {
              Plugin plugin = genPlugin(symbolValue(data, "groupId"),
                  symbolValue(data, "artifactId"),
                  symbolValue(data, "version"));
              plugins.add(plugin);
            });
          }
          if ("delete".equals(sign)) {
            dataArrays.forEach(data -> plugins.removeIf(
                plugin -> ((JSONObject) data).get("artifactId").toString().equals(plugin.getArtifactId())));
          }
          if ("replace".equals(sign)) {
            dataArrays.forEach(data -> {
              if (plugins.removeIf(plugin -> (((JSONObject) ((JSONObject) data).get("match"))
                  .getString("artifactId").equals(plugin.getArtifactId())))) {
                JSONArray replacementArrays = JSONArray.parseArray(((JSONObject) data).getString("replacement"));
                replacementArrays.forEach(replacementArray -> {
                  Plugin plugin = genPlugin(symbolValue(replacementArray, "groupId"),
                      symbolValue(replacementArray, "artifactId"),
                      symbolValue(replacementArray, "version"));
                  plugins.add(plugin);
                });
              }
            });
          }
        });
      }
    }
  }

  private void processDependencies(Model model, JSONObject pomJsonObject) {
    Object jsonPomDependencies = pomJsonObject.get("dependencies");
    List<Dependency> dependencies = model.getDependencies();
    if (!ObjectUtils.isEmpty(jsonPomDependencies) && !ObjectUtils.isEmpty(dependencies)) {
      JSONArray jsonDependencyArrays = JSONArray.parseArray(jsonPomDependencies.toString());
      jsonDependencyArrays.forEach(dependencyData -> {
        JSONObject dependencyJsonObject = JSONObject.parseObject(dependencyData.toString());
        String sign = dependencyJsonObject.get("operation").toString();
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
        if ("replace".equals(sign)) {
          dataArrays.forEach(data -> {
            if (dependencies.removeIf(dependency -> (((JSONObject) ((JSONObject) data).get("match"))
                .getString("artifactId").equals(dependency.getArtifactId())))) {
              JSONArray replacementArrays = JSONArray.parseArray(((JSONObject) data).getString("replacement"));
              replacementArrays.forEach(replacementArray -> {
                Dependency replaceDependency = genDependency(symbolValue(replacementArray, "groupId"),
                    symbolValue(replacementArray, "artifactId"),
                    symbolValue(replacementArray, "version"),
                    null,
                    null);
                dependencies.add(replaceDependency);
              });
            }
          });
        }
      });
    }
  }

  private void processDependencyManagement(Model model, JSONObject pomJsonObject) {
    Object jsonPomDependencyManagements = pomJsonObject.get("dependencyManagement.dependencies");
    DependencyManagement dependencyManagement = model.getDependencyManagement();
    if (!ObjectUtils.isEmpty(jsonPomDependencyManagements) && !ObjectUtils.isEmpty(dependencyManagement)) {
      List<Dependency> managementDependencies = dependencyManagement.getDependencies();
      JSONArray jsonDependencyManagementArrays = JSONArray.parseArray(jsonPomDependencyManagements.toString());
      jsonDependencyManagementArrays.forEach(management -> {
        JSONObject managementJsonObject = JSONObject.parseObject(management.toString());
        String sign = managementJsonObject.get("operation").toString();
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
        if ("replace".equals(sign)) {
          dataArrays.forEach(data -> {
            if (managementDependencies.removeIf(managementDependency -> (((JSONObject) ((JSONObject) data).get("match"))
                .getString("artifactId").equals(managementDependency.getArtifactId())))) {
              JSONArray replacementArrays = JSONArray.parseArray(((JSONObject) data).getString("replacement"));
              replacementArrays.forEach(replacementArray -> {
                Dependency replaceDependency = genDependency(symbolValue(replacementArray, "groupId"),
                    symbolValue(replacementArray, "artifactId"),
                    symbolValue(replacementArray, "version"),
                    symbolValue(replacementArray, "type"),
                    symbolValue(replacementArray, "scope"));
                managementDependencies.add(replaceDependency);
              });
            }
          });
        }
      });
    }
  }

  private void processProperties(Model model, JSONObject pomJsonObject) {
    Object jsonPomProperties = pomJsonObject.get("properties");
    Properties mavenProperties = model.getProperties();
    if (!ObjectUtils.isEmpty(jsonPomProperties) && !ObjectUtils.isEmpty(mavenProperties)) {
      JSONArray jsonPropertiesArrays = JSONArray.parseArray(jsonPomProperties.toString());
      jsonPropertiesArrays.forEach(property -> {
        JSONObject propertyJsonObject = JSONObject.parseObject(property.toString());
        JSONArray dataArrays = JSONArray.parseArray(propertyJsonObject.get("data").toString());
        String sign = propertyJsonObject.get("operation").toString();
        if ("add".equals(sign)) {
          dataArrays.forEach(data -> mavenProperties.putIfAbsent(
              Objects.requireNonNull(symbolValue(data, "property")),
              Objects.requireNonNull(symbolValue(data, "value"))));
        }
        if ("delete".equals(sign)) {
          dataArrays.forEach(data -> mavenProperties.remove(Objects.requireNonNull(symbolValue(data, "property"))));
        }
        if ("replace".equals(sign)) {
          dataArrays.forEach(data -> {
            String matchPropertyValue = ((JSONObject) ((JSONObject) data).get("match")).getString("property");
            if (mavenProperties.containsKey(matchPropertyValue)) {
              mavenProperties.remove(matchPropertyValue);
              JSONArray replacementArrays = JSONArray.parseArray(((JSONObject) data).getString("replacement"));
              replacementArrays.forEach(replacementArray -> mavenProperties
                  .putIfAbsent(Objects.requireNonNull(symbolValue(replacementArray, "property")),
                      Objects.requireNonNull(symbolValue(replacementArray, "value"))));
            }
          });
        }
      });
      model.setProperties(new OrderedWriteProperties(mavenProperties));
    }
  }

  private String symbolValue(Object data, String... symbol) {
    Object result = data;
    for (String s : symbol) {
      result = ((JSONObject) result).get(s);
    }
    return result == null ? null : result.toString();
  }

  private Plugin genPlugin(String groupId, String artifactId, String version) {
    Plugin plugin = new Plugin();
    plugin.setGroupId(groupId);
    plugin.setArtifactId(artifactId);
    if (!ObjectUtils.isEmpty(version)) {
      plugin.setVersion(version);
    }
    return plugin;
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
