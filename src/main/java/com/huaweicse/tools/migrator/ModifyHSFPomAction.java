package com.huaweicse.tools.migrator;

import org.springframework.stereotype.Component;

@Component
public class ModifyHSFPomAction extends ModifyPomAction {

  @Override
  public String getFrameType() {
    return "hsf";
  }
}
