package vip.gpg123.framework.interceptor.impl;

import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vip.gpg123.framework.interceptor.BasicAuthRequestInterceptor;

import java.util.Base64;

@Component
public class FrpServerBasicAuthRequestInterceptor extends BasicAuthRequestInterceptor {

  @Value("${frp.server.userName}")
  private String userName;

  @Value("${frp.server.passWord}")
  private String passWord;

  /**
   * 构建认证
   *
   * @param template tmp
   */
  @Override
  public void apply(RequestTemplate template) {
    String auth = userName + ":" + passWord;
    template.header("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));
  }
}
