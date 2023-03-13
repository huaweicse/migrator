
package com.huaweicse.tools.migrator;

public interface HSFInterfaceService {

  public ServiceResult synVipsGoodsStock(String token, Map<String,List<Map<String,Object>>> tempMap);

  List<CusShopReturnWarehouseModel> selectReturnWarehouseByInfo(String shopBrandCode, String channelCode
      , Integer warehouseType,Integer channelType, String shopCode);

  String hello(String str);

  // 无参数
  List<String> noArg();

  List<String> str(String string);

  // 单行注释
  List<String> slComment(List<String> list);

  /**
   *
   * @param map
   * @return
   */
  Map<String> muComment(Map map);

  String mix(List<String> stringList, Integer num);

  ResultBody single(EntityBody entityBody, Long count, Double num);

  ResultBody generics(Map<String, Object> entityBody, Long count, Double num);

  List<Map<String, Object>> queryWithoutOrm(String token, String sql, Object... args);

  // 该情况下打印error日志，稍后手动进行参数重构
  ResultBody manyBody(BodyOne bodyOne, BodyTwo bodyTwo, Long count, Double num);

  ServiceResult importBatchData(String token, String orderId, Map<String, List<String[]>> dataMap);
}