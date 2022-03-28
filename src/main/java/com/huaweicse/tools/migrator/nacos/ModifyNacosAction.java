package com.huaweicse.tools.migrator.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.Action;

@Component
public class ModifyNacosAction implements Action {

  private ModifyNacosAddBootstrapYamlAction modifyNacosAddBootstrapYamlAction;

  private ModifyNacosPomAction modifyNacosPomAction;

  @Autowired
  public ModifyNacosAction(
      ModifyNacosAddBootstrapYamlAction modifyNacosAddBootstrapYamlAction,
      ModifyNacosPomAction modifyNacosPomAction) {
    this.modifyNacosAddBootstrapYamlAction = modifyNacosAddBootstrapYamlAction;
    this.modifyNacosPomAction = modifyNacosPomAction;
  }

  @Override
  public void run(String... args) throws Exception {
    Action[] actions = new Action[] {modifyNacosAddBootstrapYamlAction, modifyNacosPomAction};
    for (Action action : actions) {
      action.run(args);
    }
  }
}
