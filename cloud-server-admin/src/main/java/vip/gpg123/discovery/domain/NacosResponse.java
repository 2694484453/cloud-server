package vip.gpg123.discovery.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NacosResponse<T> implements Serializable {

    private Integer code;

    private String message;

    private List<T> data;

}
