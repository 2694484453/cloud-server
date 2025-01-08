package vip.gpg123.admin.vps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaopuguang
 * @date 2024/11/6 1:57
 **/
@Data
public class Servers implements Serializable {

    private String hostname;

    private String portNumber;

    private String system;

    private String ipAddress;

    private String username;

    private String comments;
}
