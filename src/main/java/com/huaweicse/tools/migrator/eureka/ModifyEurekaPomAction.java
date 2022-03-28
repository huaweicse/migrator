package com.huaweicse.tools.migrator.eureka;

import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.ModifyPomAction;

/**
 * 功能描述：
 *   扫描目录下面的所有POM文件，删除Eureka相关的依赖、插件，增加Spring Cloud相关的依赖、插件。
 */
@Component
public class ModifyEurekaPomAction extends ModifyPomAction {
  @Override
  public String getFrameType() {
    return "eureka";
  }
}
