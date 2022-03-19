package com.huaweicse.tools.migrator.hsf;

import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.ModifyPomAction;

@Component
public class ModifyHSFPomAction extends ModifyPomAction {

  @Override
  public String getFrameType() {
    return "hsf";
  }
}
