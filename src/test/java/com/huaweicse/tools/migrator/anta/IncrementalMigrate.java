package com.huaweicse.tools.migrator.anta;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.hsf.CopyDiffFileAction;
import com.huaweicse.tools.migrator.hsf.ModifyHSFAction;

@SpringBootTest
public class IncrementalMigrate {
  @Autowired
  private ModifyHSFAction modifyHSFAction;

  @Autowired
  private CopyDiffFileAction copyDiffFileAction;

  @Test
  public void incrementalMigrate() throws Exception {
    String newTag = "D:\\anta\\anta_mw_support-final";
    String oldTag = "D:\\anta\\anta_mw_support-master";
    String diffTag = "D:\\anta\\anta_mw_support-diff";

    copyDiffFileAction.run(newTag, oldTag, diffTag);
    modifyHSFAction.run(diffTag);
  }
}
