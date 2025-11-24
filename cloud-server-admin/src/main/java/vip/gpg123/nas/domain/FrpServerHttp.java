package vip.gpg123.nas.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class FrpServerHttp implements Serializable {

    private Map<String,Object> conf;

    private String curConns;

    private String lastCloseTime;

    private String lastStartTime;

    private String name;

    private String status;

    private Integer todayTrafficIn;

    private Integer todayTrafficOut;

//    @Data
//    static
//    class Conf {
//        List<String> customDomains;
//
//        Map<String,Object> healthCheck;
//
//        String hostHeaderRewrite;
//
//        Map<String,Object> loadBalance;
//
//        String localIp;
//
//        String locations;
//
//        String name;
//
//        String plugin;
//
//        Map<String,Object> transport;
//    }
}
