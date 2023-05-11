package com.huaweicse.tools.migrator.anta;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.hsf.CheckTooManyBodyAction;
import com.huaweicse.tools.migrator.hsf.ModifyMybatisXmlAction;

@SpringBootTest
public class Check {
  @Test
  public void checkDateQuot() throws Exception {
    ModifyMybatisXmlAction action = new ModifyMybatisXmlAction();
    action.run("D:\\anta");
  }

  @Test
  public void checkTooManyBody() throws Exception {
    CheckTooManyBodyAction action = new CheckTooManyBodyAction();
    action.run("D:\\anta");
  }
}
