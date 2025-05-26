package vip.gpg123.discovery.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NacosNameSpaceResponse<T> implements Serializable {

    private Integer code;

    private String message;

    private List<T> data;

}
