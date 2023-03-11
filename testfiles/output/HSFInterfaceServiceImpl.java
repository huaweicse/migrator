
package com.huaweicse.tools.migrator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@org.springframework.context.annotation.Lazy
@RequestMapping("/hSFInterfaceService")
public class HSFInterfaceServiceImpl implements HSFInterfaceService {

  @Override
  public String hello(String name) {
    return name;
  }

}
