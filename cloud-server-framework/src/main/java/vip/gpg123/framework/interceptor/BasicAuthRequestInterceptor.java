package vip.gpg123.framework.interceptor;

import feign.RequestInterceptor;
import org.springframework.stereotype.Component;

/**
 * 基本认证
 */
@Component
public abstract class BasicAuthRequestInterceptor implements RequestInterceptor {
}
