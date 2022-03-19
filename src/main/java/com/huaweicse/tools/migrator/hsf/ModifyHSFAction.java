package com.huaweicse.tools.migrator.hsf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.Action;

@Component
public class ModifyHSFAction implements Action {

  private ModifyHSFInterface2RestAction modifyHSFInterface2RestAction;

  private ModifyHSFProviderAction modifyHSFProviderAction;

  private ModifyHSFConsumerAction modifyHSFConsumerAction;

  private ModifyHSFAddBootstrapYamlAction modifyHSFAddBootstrapYamlAction;

  private ModifyHSFPomAction modifyHSFPomAction;

  private ModifyHSFMainClassAction modifyHSFMainClassAction;

  @Autowired
  public ModifyHSFAction(ModifyHSFInterface2RestAction modifyHSFInterface2RestAction,
      ModifyHSFProviderAction modifyHSFProviderAction,
      ModifyHSFConsumerAction modifyHSFConsumerAction,
      ModifyHSFAddBootstrapYamlAction modifyHSFAddBootstrapYamlAction,
      ModifyHSFPomAction modifyHSFPomAction,
      ModifyHSFMainClassAction modifyHSFMainClassAction) {
    this.modifyHSFInterface2RestAction = modifyHSFInterface2RestAction;
    this.modifyHSFProviderAction = modifyHSFProviderAction;
    this.modifyHSFConsumerAction = modifyHSFConsumerAction;
    this.modifyHSFAddBootstrapYamlAction = modifyHSFAddBootstrapYamlAction;
    this.modifyHSFPomAction = modifyHSFPomAction;
    this.modifyHSFMainClassAction = modifyHSFMainClassAction;
  }

  @Override
  public void run(String... args) throws Exception {
    Action[] actions = new Action[] {modifyHSFInterface2RestAction, modifyHSFProviderAction
        , modifyHSFConsumerAction, modifyHSFAddBootstrapYamlAction, modifyHSFPomAction, modifyHSFMainClassAction};
    for (Action action : actions) {
      action.run(args);
    }
  }
}
