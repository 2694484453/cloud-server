package vip.gpg123.framework.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gaopuguang
 * @date 2025/2/16 18:51
 **/
@Data
@Component
@ConfigurationProperties(prefix = "ide")
public class IdeClient {

    private String path;
}
