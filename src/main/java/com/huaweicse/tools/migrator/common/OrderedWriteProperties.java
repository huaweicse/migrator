package com.huaweicse.tools.migrator.common;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

// ordered write properties
public class OrderedWriteProperties extends Properties {
  public OrderedWriteProperties(Properties other) {
    super();
    putAll(other);
  }

  @Override
  public Set<Object> keySet() {
    return new LinkedHashSet<>(super.keySet());
  }
}
