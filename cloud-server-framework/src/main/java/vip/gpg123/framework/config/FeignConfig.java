package vip.gpg123.framework.config;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FeignConfig {

    /**
     * 常用远程调用RestTemplate
     * @return restTemplate
     */
    @Bean("restTemplate")
    @LoadBalanced
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        // 加入自定义信息转换
        restTemplate.getMessageConverters().add(new FeignConfig.CustomJacksonConverter());
        return new RestTemplate();
    }

    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        return () -> new HttpMessageConverters(new CustomJacksonConverter());
    }

    // 扩展Jackson转换器，支持text/plain
    public static class CustomJacksonConverter extends MappingJackson2HttpMessageConverter {
        public CustomJacksonConverter() {
            // 添加text/plain到支持的媒体类型
            List<MediaType> mediaTypes = new ArrayList<>(getSupportedMediaTypes());
            mediaTypes.add(MediaType.TEXT_PLAIN);
            setSupportedMediaTypes(mediaTypes);
        }
    }
}
