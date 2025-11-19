package vip.gpg123.app.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.app.domain.HelmAppMarket;

@EqualsAndHashCode(callSuper = true)
@Data
public class HelmAppMarketVo extends HelmAppMarket {

    private JSONObject values;
}
