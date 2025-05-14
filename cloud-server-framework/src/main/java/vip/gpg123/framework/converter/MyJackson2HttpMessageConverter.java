package vip.gpg123.framework.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class MyJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    //fegin接收content-type:text/plain 返回体的解决方案
    public MyJackson2HttpMessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypes);
    }
}