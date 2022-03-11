package com.huaweicse.tools.migrator;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



/**
 * 功能描述：
 * 扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFProvider 标签，如果存在，将其替换为 @RestController。
 * 替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFProviderAction implements Action {
  public static String PROJECT_PATH = System.getProperty("user.dir");
  @Override
  public void run(String... args) {
    System.out.println(PROJECT_PATH);
    Path start = FileSystems.getDefault().getPath(PROJECT_PATH+"/testfiles/output");
    try {
      List<Path> list = Files.walk(start).filter(childpath -> childpath.toFile().isFile()).filter(path -> path.toString().endsWith(".java")).filter(path -> ifHasAnnotion(path)).collect(Collectors.toList());
      list.forEach(path -> {
        replace(path);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean ifHasAnnotion(Path path) {
    /*
     * @Author MrLi
     * @Description 判断文件是不是包含指定注解
     * @Date 11:32 2022/3/11
     * @Param [java.nio.file.Path]
     * @return boolean
     **/
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(path.toFile()));
      String tempString = null;
      int line = 1;
      // 一次读入一行，直到读入null为文件结束
      while ((tempString = reader.readLine()) != null) {
        if (tempString.contains("@HSFProvider")) {
          reader.close();
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    return false;
  }

  public void replace(Path path) {
    System.out.println(path.toString());
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(path.toFile()));
      PrintWriter pw = null;
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile(), true)));
      String tempString = null;
      StringBuffer buff = new StringBuffer();
      String separator = null;//平台换行!
      int line = 1;
      // 一次读入一行，直到读入null为文件结束
      while ((tempString = reader.readLine()) != null) {
        separator = System.getProperty("line.separator");
        if (tempString.contains("@HSFProvider")) {
          // 显示行号
          System.out.println("line " + line + ": " + tempString + path.toString());
          tempString = "@RestController";
        }else if (tempString.contains("implements")){
          String[] strings = tempString.substring(0, tempString.length() -1).split(" ");
          System.out.println(strings[strings.length-1]);
          //System.out.println(strings[strings.length-1].replaceFirst(strings[strings.length-1].substring(0,1), strings[strings.length-1].substring(0,1).toLowerCase()));
          String requestpath = strings[strings.length-1].replaceFirst(strings[strings.length-1].substring(0,1), strings[strings.length-1].substring(0,1).toLowerCase());
          tempString = "@RequestMapping(\""+requestpath+"\")"+separator+tempString;
        }else if (tempString.contains("HSFProvider")&&tempString.contains("import")){
          tempString = imporClass();
          separator = "";
        }
        buff.append(tempString + separator);
        line++;
      }
      reader.close();
      pw = new PrintWriter(new FileWriter(path.toFile()), true);
      pw.println(buff);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }

  }

  public boolean addXml(String url, String paramKey, String paramValue,String versionValue) {
    /*
     * @Author MrLi
     * @Description //增加pom依赖
     * @Date 10:50 2022/3/11
     * @Param [java.lang.String, java.lang.String, java.lang.String, java.lang.String]
     * @return boolean
     **/
    MavenXpp3Reader reader = new MavenXpp3Reader();
    String pomUrl = url+"\\pom.xml";

    String groupId = "";
    String artifactId = "";
    String version = "";

    //groupId
    String groupRgex = "<groupId>(.*?)</groupId>";
    groupId = getParam(paramKey, groupRgex);
    String groupStr = groupId.toString();
    //artifactId
    String artifactRgex = "<artifactId>(.*?)</artifactId>";
    artifactId = getParam(paramValue, artifactRgex);
    String artifactIdStr = artifactId.toString();
    //version
    String versionRgex = "<version>(.*?)</version>";
    version = getParam(versionValue, versionRgex);
    try {
      FileInputStream fis = new FileInputStream(new File(pomUrl));
      Model model = reader.read(fis);
      List<Dependency> dependencies = model.getDependencies();
      dependencies = dependencies.stream().filter(dependency -> !dependency.getGroupId().equals(groupStr)&&!dependency.getArtifactId().equals(artifactIdStr)).collect(Collectors.toList());
      Dependency addDependency = new Dependency();
      addDependency.setGroupId(groupId);
      addDependency.setArtifactId(artifactId);
      addDependency.setVersion(version);
      dependencies.add(addDependency);
      model.setDependencies(dependencies);
      MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
      mavenXpp3Writer.write(new FileWriter(pomUrl),model);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    }
    return true;
  }

  public String imporClass(){
    /*
     * @Author MrLi
     * @Description 导入替换注解所需要生成的包
     * @Date 19:41 2022/3/9
     * @Param [java.lang.String]
     * @return void
     **/
    //初始化ASTParser
    ASTParser astParser = ASTParser.newParser(AST.JLS8);
    astParser.setSource(new char[] {});
    ASTNode astnode = astParser.createAST(null);
    AST ast = astnode.getAST();
    ImportDeclaration importDeclaration1 = ast.newImportDeclaration();
    ImportDeclaration importDeclaration2 = ast.newImportDeclaration();
    importDeclaration1.setName(ast.newName("org.springframework.web.bind.annotation.RequestMapping"));
    importDeclaration2.setName(ast.newName("org.springframework.web.bind.annotation.RestController"));
    String imports = importDeclaration1.toString() + importDeclaration2.toString();
    System.out.println(importDeclaration1.toString() + importDeclaration2.toString() + "222222222");
    return imports;
  }

  private String getParam(String param, String rgex) {
    /*
     * @Author MrLi
     * @Description 获取匹配模式的正则
     * @Date 14:52 2022/3/11
     * @Param [java.lang.String, java.lang.String]
     * @return java.lang.String
     **/
    Pattern groupPattern = Pattern.compile(rgex);// 匹配的模式
    Matcher group = groupPattern.matcher(param);
    String id = "";
    while(group.find()){
      id = group.group(1);
    }
    return id;
  }

  public static void main(String[] args) {
    try {
      FileUtil.copyFolder("D:\\m\\migrator\\testfiles\\input","D:\\m\\migrator\\testfiles\\output");
      new ModifyHSFProviderAction().run();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}



