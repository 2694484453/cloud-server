package vip.gpg123.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate配置类
 */
@Slf4j
@Configuration
public class RestTemplateConfig {

    /**
     * 常用远程调用RestTemplate
     * @return restTemplate
     */
    @Bean("restTemplate")
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        // 加入自定义信息转换
        restTemplate.getMessageConverters().add(new MyMappingJackson2HttpMessageConverter());
        return new RestTemplate();
    }


    /**
     * 使RestTemplate支持转换类型为text/plain的数据
     */
    public static class MyMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public MyMappingJackson2HttpMessageConverter(){
            List<MediaType> mediaTypes = new ArrayList<>();
            // 加入text/plain类型的支持
            mediaTypes.add(MediaType.ALL);
            // 如果还有其他类型的需要装换，可以一一加上
            setSupportedMediaTypes(mediaTypes);
        }
    }
}