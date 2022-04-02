package com.huaweicse.tools.migrator.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Const implements InitializingBean {

  @Value("${spring.responseBody.packageName:org.springframework.web.bind.annotation.ResponseBody}")
  private String responseBodyPackageName;

  @Value("${spring.postMapping.packageName:org.springframework.web.bind.annotation.PostMapping}")
  private String postMappingPackageName;

  @Value("${spring.requestParam.packageName:org.springframework.web.bind.annotation.RequestParam}")
  private String requestParamPackageName;

  @Value("${spring.requestBody.packageName:org.springframework.web.bind.annotation.RequestBody}")
  private String requestBodyPackageName;

  @Value("${spring.propertySource.packageName:org.springframework.context.annotation.PropertySource}")
  private String propertySourcePackageName;

  @Value("${spring.enableFeignClients.packageName:org.springframework.cloud.openfeign.EnableFeignClients}")
  private String enableFeignClientsPackageName;

  @Value("${spring.importResource.packageName:org.springframework.context.annotation.ImportResource}")
  private String importResourcePackageName;

  @Value("${spring.autowired.packageName:org.springframework.beans.factory.annotation.Autowired}")
  private String autowiredPackageName;

  @Value("${spring.feignClient.packageName:org.springframework.cloud.openfeign.FeignClient}")
  private String feignClientPackageName;

  @Value("${spring.configuration.packageName:org.springframework.context.annotation.Configuration}")
  private String configurationPackageName;

  @Value("${spring.requestMapping.packageName:org.springframework.web.bind.annotation.RequestMapping}")
  private String requestMappingPackageName;

  @Value("${spring.restController.packageName:org.springframework.web.bind.annotation.RestController}")
  private String restControllerPackageName;

  @Value("${spring.enableDiscoveryClient.packageName:org.springframework.cloud.client.discovery.EnableDiscoveryClient}")
  private String enableDiscoveryClientPackageName;

  @Value("${spring.enableEurekaClient.packageName:org.springframework.cloud.netflix.eureka.EnableEurekaClient}")
  private String enableEurekaClientPackageName;

  @Value("${spring.enableEurekaServer.packageName:org.springframework.cloud.netflix.eureka.server.EnableEurekaServer}")
  private String enableEurekaServerPackageName;

  @Value("${dubbo.enableDubbo.packageName:org.apache.dubbo.config.spring.context.annotation.EnableDubbo}")
  private String enableDubboPackageName;

  @Value("${dubbo.dubboReference.packageName:org.apache.dubbo.config.annotation.DubboReference}")
  private String dubboReferencePackageName;

  @Value("${dubbo.provider.packageName:org.apache.dubbo.config.annotation.DubboService}")
  private String dubboProviderPackageName;

  @Value("${hsf.consumer.packageName:com.alibaba.boot.hsf.annotation.HSFConsumer}")
  private String hsfConsumerPackageName;

  @Value("${hsf.provider.packageName:com.alibaba.boot.hsf.annotation.HSFProvider}")
  private String hSFProviderPackageName;

  @Value("${hsf.pandoraBootstrap.packageName:com.taobao.pandora.boot.PandoraBootstrap}")
  private String pandoraBootstrapPackageName;

  public static String ENABLE_DISCOVERY_CLIENT_PACKAGE_NAME;

  public static String ENABLE_FEIGN_CLIENTS_PACKAGE_NAME;

  public static String PROPERTY_SOURCE_PACKAGE_NAME;

  public static String IMPORT_RESOURCE_PACKAGE_NAME;

  public static String REST_CONTROLLER_PACKAGE_NAME;

  public static String REQUEST_MAPPING_PACKAGE_NAME;

  public static String RESPONSE_BODY_PACKAGE_NAME;

  public static String POST_MAPPING_PACKAGE_NAME;

  public static String REQUEST_PARAM_PACKAGE_NAME;

  public static String REQUEST_BODY_PACKAGE_NAME;

  public static String AUTOWIRED_PACKAGE_NAME;

  public static String FEIGN_CLIENT_PACKAGE_NAME;

  public static String CONFIGURATION_PACKAGE_NAME;

  public static String ENABLE_EUREKA_CLIENT_PACKAGE_NAME;

  public static String ENABLE_EUREKA_SERVER_PACKAGE_NAME;

  public static String ENABLE_DUBBO_PACKAGE_NAME;

  public static String DUBBO_REFERENCE_PACKAGE_NAME;

  public static String DUBBO_PROVIDER_PACKAGE_NAME;

  public static String HSF_CONSUMER_PACKAGE_NAME;

  public static String HSF_PROVIDER_PACKAGE_NAME;

  public static String PANDORA_BOOT_STRAP_PACKAGE_NAME;

  @Override
  public void afterPropertiesSet() {
    ENABLE_DISCOVERY_CLIENT_PACKAGE_NAME = enableDiscoveryClientPackageName;
    ENABLE_FEIGN_CLIENTS_PACKAGE_NAME = enableFeignClientsPackageName;
    PROPERTY_SOURCE_PACKAGE_NAME = propertySourcePackageName;
    IMPORT_RESOURCE_PACKAGE_NAME = importResourcePackageName;
    REST_CONTROLLER_PACKAGE_NAME = restControllerPackageName;
    REQUEST_MAPPING_PACKAGE_NAME = requestMappingPackageName;
    RESPONSE_BODY_PACKAGE_NAME = responseBodyPackageName;
    POST_MAPPING_PACKAGE_NAME = postMappingPackageName;
    REQUEST_PARAM_PACKAGE_NAME = requestParamPackageName;
    REQUEST_BODY_PACKAGE_NAME = requestBodyPackageName;
    AUTOWIRED_PACKAGE_NAME = autowiredPackageName;
    FEIGN_CLIENT_PACKAGE_NAME = feignClientPackageName;
    CONFIGURATION_PACKAGE_NAME = configurationPackageName;
    ENABLE_EUREKA_CLIENT_PACKAGE_NAME = enableEurekaClientPackageName;
    ENABLE_EUREKA_SERVER_PACKAGE_NAME = enableEurekaServerPackageName;
    ENABLE_DUBBO_PACKAGE_NAME = enableDubboPackageName;
    DUBBO_REFERENCE_PACKAGE_NAME = dubboReferencePackageName;
    DUBBO_PROVIDER_PACKAGE_NAME = dubboProviderPackageName;
    HSF_CONSUMER_PACKAGE_NAME = hsfConsumerPackageName;
    HSF_PROVIDER_PACKAGE_NAME = hSFProviderPackageName;
    PANDORA_BOOT_STRAP_PACKAGE_NAME = pandoraBootstrapPackageName;
  }
}
