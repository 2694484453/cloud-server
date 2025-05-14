package vip.gpg123.git.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class FrpServerHttp implements Serializable {

    private String conf;

    private String curConns;

    private String lastCloseTime;

    private String lastStartTime;

    private String name;

    private String status;

    private Integer todayTrafficIn;

    private Integer todayTrafficOut;
}
