package vip.gpg123.git.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class FrpServerHttp implements Serializable {

    private Map<String, Object> conf;

    private String curConns;

    private String lastCloseTime;

    private String lastStartTime;

    private String name;

    private String status;

    private Integer todayTrafficIn;

    private Integer todayTrafficOut;
}
