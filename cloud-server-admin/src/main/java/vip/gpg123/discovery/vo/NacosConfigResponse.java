package vip.gpg123.discovery.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NacosConfigResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<NacosConfigItem> pageItems;

    private Integer pageNum;

    private Integer pageSize;

    private Integer totalCount;

    private Integer pageAvailable;
}
