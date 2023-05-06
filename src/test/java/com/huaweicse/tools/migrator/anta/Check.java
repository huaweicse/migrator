package com.huaweicse.tools.migrator.anta;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.hsf.CopyDiffFileAction;
import com.huaweicse.tools.migrator.hsf.ModifyMybatisXmlAction;

@SpringBootTest
public class Check {
  @Test
  public void checkDateQuot() throws Exception {
    ModifyMybatisXmlAction action = new ModifyMybatisXmlAction();
    action.run("D:\\anta");
  }

  @Test
  public void copyDiffFile()  throws Exception {
    CopyDiffFileAction action = new CopyDiffFileAction();
    action.run("D:\\anta\\anta_mw_support-final",
        "D:\\anta\\anta_mw_support-master",
        "D:\\anta\\anta_mw_support-diff");
  }
}
