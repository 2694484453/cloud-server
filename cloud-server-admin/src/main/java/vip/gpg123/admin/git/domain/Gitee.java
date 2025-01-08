package vip.gpg123.admin.git.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gaopuguang
 * @date 2024/11/30 3:59
 **/
@Data
@Component
@ConfigurationProperties(prefix = "git.gitee")
public class Gitee {

    private String grant_type;

    private String redirect_uri;

    private String client_id;

    private String client_secret;

    private String api;
}
