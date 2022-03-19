package com.huaweicse.tools.migrator.dubbo;

import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.ModifyPomAction;

@Component
public class ModifyDubboPomAction extends ModifyPomAction {
  @Override
  public String getFrameType() {
    return "dubbo";
  }
}
