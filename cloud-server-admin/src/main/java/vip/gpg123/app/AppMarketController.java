package vip.gpg123.app;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.HelmAppMarket;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.service.HelmAppMarketService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.DateUtils;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/app/market")
@Api(value = "应用市场")
public class AppMarketController extends BaseController {

    @Value("${repo.helm.url}")
    private String helmUrl;

    @Autowired
    private HelmAppMarketService helmAppMarketService;

    @Autowired
    private AppManagerController appManagerController;


    /**
     * 列表查询
     *
     * @param name   名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "version", required = false) String version,
                           @RequestParam(value = "status", required = false) String status) {
        // 查询
        List<HelmAppMarket> helmAppMarkets = helmAppMarketService.list(new LambdaQueryWrapper<HelmAppMarket>()
                        .like(StrUtil.isNotBlank(name), HelmAppMarket::getName, name)
                        .like(StrUtil.isNotBlank(version), HelmAppMarket::getVersion, version)
                        .eq(StrUtil.isNotBlank(status), HelmAppMarket::getStatus, status)
                //.eq(HelmAppMarket::getCreateBy, getUsername())
        );
        return AjaxResult.success(helmAppMarkets);
    }

    /**
     * 分页查询
     *
     * @param page   page
     * @param name   名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(Page<HelmAppMarket> page,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "version", required = false) String version,
                              @RequestParam(value = "status", required = false) String status) {
        // 获取分页参数
        PageUtils.toIPage(page);
        IPage<HelmAppMarket> pageRes = helmAppMarketService.page(page, new LambdaQueryWrapper<HelmAppMarket>()
                        .like(StrUtil.isNotBlank(name), HelmAppMarket::getName, name)
                        .like(StrUtil.isNotBlank(version), HelmAppMarket::getVersion, version)
                        .eq(StrUtil.isNotBlank(status), HelmAppMarket::getStatus, status)
                //.eq(HelmAppMarket::getCreateBy, getUsername())
        );
        return PageUtils.toPageByIPage(pageRes);
    }

    /**
     * 删除
     *
     * @param helmAppMarket h
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestBody HelmAppMarket helmAppMarket) {
        boolean del = helmAppMarketService.removeById(helmAppMarket.getId());
        return del ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 安装
     * @return r
     */
    @PostMapping("/install")
    @ApiOperation(value = "安装")
    public AjaxResult install(@RequestBody HelmAppMarket helmAppMarket) {
        MineApp mineApp = new MineApp();
        mineApp.setAppName(helmAppMarket.getName());
        mineApp.setChartName(helmAppMarket.getName());
        mineApp.setDescription("");
        mineApp.setCreateBy(SecurityUtils.getUsername());
        mineApp.setUpdateBy(SecurityUtils.getUsername());
        mineApp.setCreateTime(DateUtils.getNowDate());
        mineApp.setNameSpace(SecurityUtils.getUserId().toString());

        return appManagerController.install(mineApp);
    }
}
