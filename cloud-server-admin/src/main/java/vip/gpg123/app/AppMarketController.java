package vip.gpg123.app;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.HelmAppMarket;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.service.HelmAppMarketService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/market")
@Api(value = "应用市场")
public class AppMarketController extends BaseController {

    @Value("${repo.helm.url}")
    private String helmUrl;

    @Autowired
    private HelmAppMarketService helmAppMarketService;

    /**
     * 列表查询
     *
     * @param appName   名称
     * @param chartName chart名称
     * @param status    状态
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "appName", required = false) String appName,
                           @RequestParam(value = "chartName", required = false) String chartName,
                           @RequestParam(value = "status", required = false) String status) {
        // 查询
        List<HelmAppMarket> helmAppMarkets = helmAppMarketService.list(new LambdaQueryWrapper<HelmAppMarket>()
                .like(StrUtil.isNotBlank(appName), HelmAppMarket::getName, appName)
                .like(StrUtil.isNotBlank(chartName), HelmAppMarket::getName, chartName)
                .eq(StrUtil.isNotBlank(status), HelmAppMarket::getStatus, status)
                .eq(HelmAppMarket::getCreateBy, getUsername())
        );
        return AjaxResult.success(helmAppMarkets);
    }

    /**
     * 分页查询
     * @param page page
     * @param appName 名称
     * @param chartName chart名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(Page<HelmAppMarket> page,
                              @RequestParam(value = "appName", required = false) String appName,
                              @RequestParam(value = "chartName", required = false) String chartName,
                              @RequestParam(value = "status", required = false) String status) {
        // 获取分页参数
        PageUtils.toIPage(page);
        IPage<HelmAppMarket> pageRes = helmAppMarketService.page(page, new LambdaQueryWrapper<HelmAppMarket>());
        return PageUtils.toPageByIPage(pageRes);
    }

    /**
     * 同步
     * @return
     */
    @GetMapping
    public TableDataInfo install() {

        return null;
    }
}
