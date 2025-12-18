package vip.gpg123.app.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.app.domain.HelmAppMarket;

@EqualsAndHashCode(callSuper = true)
@Data
public class HelmAppMarketVo extends HelmAppMarket {

    private String releaseName;

    private String kubeContext;

    private String nameSpace;

    private Object chartValues;
}
