package vip.gpg123.framework.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gaopuguang
 * @date 2025/2/16 20:20
 **/
@Data
@Component
@ConfigurationProperties(prefix = "remote")
public class RemoteClient {

    private String host;

    private String port;

    private String userName;

    private String passWord;
}
