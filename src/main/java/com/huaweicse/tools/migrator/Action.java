package com.huaweicse.tools.migrator;

public interface Action {
  default String name() {
    return this.getClass().getSimpleName();
  }

  void run(String... args);
}
