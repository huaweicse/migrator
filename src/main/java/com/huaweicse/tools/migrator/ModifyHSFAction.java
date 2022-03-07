package com.huaweicse.tools.migrator;

public class ModifyHSFAction implements Action {
  @Override
  public void run(String... args) {
    Action[] actions = new Action[] {new ModifyHSFInterface2RestAction(),
        new ModifyHSFProviderAction(), new ModifyHSFConsumerAction()};
    for (Action action : actions) {
      action.run(args);
    }
  }
}
