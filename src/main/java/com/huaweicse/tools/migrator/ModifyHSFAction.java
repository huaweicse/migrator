package com.huaweicse.tools.migrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyHSFAction implements Action {

  private ModifyHSFInterface2RestAction modifyHSFInterface2RestAction;

  private ModifyHSFProviderAction modifyHSFProviderAction;

  private ModifyHSFConsumerAction modifyHSFConsumerAction;

  private ModifyHSFAddBootstrapYamlAction modifyHSFAddBootstrapYamlAction;

  @Autowired
  public ModifyHSFAction(ModifyHSFInterface2RestAction modifyHSFInterface2RestAction,
      ModifyHSFProviderAction modifyHSFProviderAction,
      ModifyHSFConsumerAction modifyHSFConsumerAction,
      ModifyHSFAddBootstrapYamlAction modifyHSFAddBootstrapYamlAction) {
    this.modifyHSFInterface2RestAction = modifyHSFInterface2RestAction;
    this.modifyHSFProviderAction = modifyHSFProviderAction;
    this.modifyHSFConsumerAction = modifyHSFConsumerAction;
    this.modifyHSFAddBootstrapYamlAction = modifyHSFAddBootstrapYamlAction;
  }

  @Override
  public void run(String... args) throws Exception {
    Action[] actions = new Action[] {modifyHSFInterface2RestAction, modifyHSFProviderAction
        , modifyHSFConsumerAction, modifyHSFAddBootstrapYamlAction};
    for (Action action : actions) {
      action.run(args);
    }
  }
}
