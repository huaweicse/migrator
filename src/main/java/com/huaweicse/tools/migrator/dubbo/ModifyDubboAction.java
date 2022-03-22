package com.huaweicse.tools.migrator.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.Action;

@Component
public class ModifyDubboAction implements Action {

  private ModifyDubboInterface2RestAction modifyDubboInterface2RestAction;

  private ModifyDubboAddBootstrapYamlAction modifyDubboAddBootstrapYamlAction;

  private ModifyDubboMainClassAction modifyDubboMainClassAction;

  private ModifyDubboPomAction modifyDubboPomAction;

  private  ModifyDubboReferenceAction modifyDubboReferenceAction;

  private ModifyDubboServiceAction modifyDubboServiceAction;

  @Autowired
  public ModifyDubboAction(
      ModifyDubboInterface2RestAction modifyDubboInterface2RestAction,
      ModifyDubboAddBootstrapYamlAction modifyDubboAddBootstrapYamlAction,
      ModifyDubboMainClassAction modifyDubboMainClassAction,
      ModifyDubboPomAction modifyDubboPomAction,
      ModifyDubboReferenceAction modifyDubboReferenceAction,
      ModifyDubboServiceAction modifyDubboServiceAction) {
    this.modifyDubboInterface2RestAction = modifyDubboInterface2RestAction;
    this.modifyDubboAddBootstrapYamlAction = modifyDubboAddBootstrapYamlAction;
    this.modifyDubboMainClassAction = modifyDubboMainClassAction;
    this.modifyDubboPomAction = modifyDubboPomAction;
    this.modifyDubboReferenceAction = modifyDubboReferenceAction;
    this.modifyDubboServiceAction = modifyDubboServiceAction;
  }

  @Override
  public void run(String... args) throws Exception {
    Action[] actions = new Action[] {modifyDubboInterface2RestAction, modifyDubboAddBootstrapYamlAction
        , modifyDubboMainClassAction, modifyDubboPomAction, modifyDubboReferenceAction, modifyDubboServiceAction};
    for (Action action : actions) {
      action.run(args);
    }
  }

}
