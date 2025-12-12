package vip.gpg123.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.HelmApp;
import vip.gpg123.app.service.HelmAppMarketService;
import vip.gpg123.app.service.HelmAppService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app")
@Api(tags = "应用统计")
public class AppOverViewController extends BaseController {

    @Autowired
    private HelmAppService helmAppService;

    @Autowired
    private HelmAppMarketService helmAppMarketService;

    /**
     * 应用概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "应用概览")
    public AjaxResult overView() {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> appMarket = new HashMap<>();
        appMarket.put("title","应用市场应用总数");
        appMarket.put("count",helmAppMarketService.count());
        list.add(appMarket);

        Map<String,Object> map = new LinkedHashMap<>();
        // 应用总数量
        map.put("title", "我的应用总数量");
        map.put("count", helmAppService.count(new LambdaQueryWrapper<HelmApp>()
                .eq(HelmApp::getCreateBy,  getUserId())
        ));
        list.add(map);
        return AjaxResult.success(list);
    }

}
