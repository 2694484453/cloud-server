package vip.gpg123.common.config.serializer;

import cn.hutool.json.JSONNull;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: jsonNull序列化
 * @date 2025/1/20 17:06
 */
@JsonComponent
public class JsonNullSerializer extends JsonSerializer<JSONNull> {

    @Override
    public void serialize(JSONNull jsonNull, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNull();
    }
}
