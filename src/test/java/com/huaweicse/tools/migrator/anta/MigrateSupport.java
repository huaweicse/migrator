package com.huaweicse.tools.migrator.anta;

import com.huaweicse.tools.migrator.hsf.GenerateHSFConsumerAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFAddBootstrapYamlAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFInterface2RestAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFMainClassAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFPomAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFProviderAction;
import com.huaweicse.tools.migrator.hsf.ReadHSFInfoAction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MigrateSupport {
//  private String BASE_PATH;
//
//  private String TEMP_DIR_PATH;

  // TODO: generics parameter detection

  @Autowired
  private ModifyHSFAction modifyHSFAction;

  @Autowired
  private ModifyHSFInterface2RestAction modifyHSFInterface2RestAction;

  @Autowired
  private ModifyHSFProviderAction modifyHSFProviderAction;

  @Autowired
  private ReadHSFInfoAction readHSFInfoAction;


  @BeforeEach
  public void setUp() throws Exception {
//    TEMP_DIR_PATH = "D:\\repo\\temp";
//    FileUtils.copyDirectoryToDirectory(
//        new File(BASE_PATH),
//        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
//    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testModifyHSFInterface2Rest() throws Exception {
    modifyHSFInterface2RestAction.run("D:\\GitRepos\\QDport\\current\\V1.0\\newlog\\newlog-api");
  }

  @Test
  public void testModifyHSFProvider() throws Exception {
    modifyHSFProviderAction.run("D:\\GitRepos\\QDport\\current\\V1.0\\newlog\\newlog-service");
  }

  @Test
  public void testModifyHSFConsumer() throws Exception {
    GenerateHSFConsumerAction action = new GenerateHSFConsumerAction();
    action.run("D:\\GitRepos\\QDport\\current\\V1.0\\newlog\\newlog-web", "spring-consumer.xml",
        "com.qdport.log.config", "NewlogConfiguration");
  }
}
