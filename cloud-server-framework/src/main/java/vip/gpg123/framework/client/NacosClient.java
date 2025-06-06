package vip.gpg123.framework.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NacosClient implements Serializable {

    private String serverAddr;

    private String namespace;

    private String group;

    private String userName;

    private String password;
}
