package vip.gpg123.discovery.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NacosService implements Serializable {

    private Integer count;

    private List<String> doms;
}
