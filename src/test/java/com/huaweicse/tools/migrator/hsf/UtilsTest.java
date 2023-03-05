package com.huaweicse.tools.migrator.hsf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.huaweicse.tools.migrator.common.Parameter;

public class UtilsTest {
  @Test
  public void testIsMethod() {
    Assertions.assertTrue(ModifyHSFInterface2RestAction.isMethod("    String when();"));
    Assertions.assertTrue(ModifyHSFInterface2RestAction.isMethod("    String echo(String string);"));
    Assertions.assertTrue(ModifyHSFInterface2RestAction.isMethod("    ServiceResult enable(String token, Long id);"));

    Parameter[] parameters = ModifyHSFInterface2RestAction.methodParameters("    String when();", null, 0);
    Assertions.assertEquals(0, parameters.length);
    parameters = ModifyHSFInterface2RestAction.methodParameters("    String echo(String string);", null, 0);
    Assertions.assertEquals(1, parameters.length);
    Assertions.assertEquals("String", parameters[0].type);
    Assertions.assertEquals("string", parameters[0].name);
    parameters = ModifyHSFInterface2RestAction.methodParameters("    ServiceResult enable(String token, Long id);",
        null, 0);
    Assertions.assertEquals(2, parameters.length);
    Assertions.assertEquals("String", parameters[0].type);
    Assertions.assertEquals("token", parameters[0].name);
    Assertions.assertEquals("Long", parameters[1].type);
    Assertions.assertEquals("id", parameters[1].name);
  }
}
