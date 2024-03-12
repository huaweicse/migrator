package com.huaweicse.tools.migrator.anta;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.hsf.GenerateHSFConsumerAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFInterface2RestAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFProviderAction;
import com.huaweicse.tools.migrator.hsf.ReadHSFInfoAction;

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
//    TEMP_DIR_PATH = "D:\\anta\\temp";
//    BASE_PATH = "D:\\anta\\anta_mw_finance_app";
//    BASE_PATH = "D:\\Projects\\2024-009-青岛港\\user\\user";
//    BASE_PATH = "D:\\anta\\anta_m w_operate_app";
//    BASE_PATH = "D:\\anta\\anta_mw_distribute_app-final";
//    BASE_PATH = "D:\\anta\\anta_mw_retail_app-final";
//    BASE_PATH = "D:\\anta\\anta_mw_customer-final";
//    BASE_PATH = "D:\\anta\\anta_mw_customers-final";
//    BASE_PATH = "D:\\anta\\anta_mw_stock";
//    BASE_PATH = "D:\\anta\\anta_mw_distributes-final";
//    BASE_PATH = "D:\\anta\\anta_mw_report-final";
//    BASE_PATH = "D:\\anta\\anta_mw_market-final";
//    BASE_PATH = "D:\\anta\\anta_mw_settlement";
//    BASE_PATH = "D:\\anta\\anta_mw_gateway_app-final";
//    BASE_PATH = "D:\\anta\\anta_mw_gateway_in_app-final";
//    BASE_PATH = "D:\\anta\\anta_mw_order-final";
//    BASE_PATH = "D:\\anta\\anta_mw_goods";
//    BASE_PATH = "D:\\anta\\anta_mw_gateway_app";
//    BASE_PATH = "D:\\anta\\anta_mw_support";
//    BASE_PATH = "D:\\anta\\anta_mw_alarm_app-final";
//    BASE_PATH = "D:\\anta\\anta_mw_operate_app-final";
//    BASE_PATH = "D:\\anta\\anta_mw_core";
//    BASE_PATH = "D:\\anta\\anta_mw_task_app-final";
//    BASE_PATH = "D:\\anta\\anta_mw_pos_order";
//
//    FileUtils.copyDirectoryToDirectory(
//        new File(BASE_PATH),
//        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
//    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testGenerateHSFConsumerAction() throws Exception {
    GenerateHSFConsumerAction action = new GenerateHSFConsumerAction();
    action.run("D:\\Projects\\2024-009-青岛港\\user\\api", "hsf-consumer-user.xml",
        "com.qdport.v2.user.config", "UserServiceConfiguration");
  }

  @Test
  public void testReadHSFInfoAction() throws Exception {
    readHSFInfoAction.run("D:\\Projects\\2024-009-青岛港\\user\\user");
  }

  @Test
  public void testModifyHSF() throws Exception {
    modifyHSFInterface2RestAction.run("D:\\Projects\\2024-009-青岛港\\user\\user\\user-api");
    modifyHSFProviderAction.run("D:\\Projects\\2024-009-青岛港\\user\\user\\user-service");
  }
}
