package com.huaweicse.tools.migrator.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.Action;

@Component
public class ModifyEurekaAction implements Action {

  private ModifyEurekaAddBootstrapYamlAction modifyEurekaAddBootstrapYamlAction;

  private ModifyEurekaMainClassAction modifyEurekaMainClassAction;

  private ModifyEurekaPomAction modifyEurekaPomAction;

  @Autowired
  public ModifyEurekaAction(
      ModifyEurekaAddBootstrapYamlAction modifyEurekaAddBootstrapYamlAction,
      ModifyEurekaMainClassAction modifyEurekaMainClassAction,
      ModifyEurekaPomAction modifyEurekaPomAction) {
    this.modifyEurekaAddBootstrapYamlAction = modifyEurekaAddBootstrapYamlAction;
    this.modifyEurekaMainClassAction = modifyEurekaMainClassAction;
    this.modifyEurekaPomAction = modifyEurekaPomAction;
  }

  @Override
  public void run(String... args) throws Exception {
    Action[] actions = new Action[] {modifyEurekaAddBootstrapYamlAction, modifyEurekaMainClassAction
        , modifyEurekaPomAction};
    for (Action action : actions) {
      action.run(args);
    }
  }
}
