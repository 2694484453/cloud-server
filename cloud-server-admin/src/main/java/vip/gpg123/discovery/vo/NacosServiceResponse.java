package vip.gpg123.discovery.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NacosServiceResponse implements Serializable {

    private Integer count;

    private List<String> doms;
}
