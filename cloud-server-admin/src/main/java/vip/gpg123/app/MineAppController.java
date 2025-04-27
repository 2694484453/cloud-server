package vip.gpg123.app;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.HelmApp;
import vip.gpg123.app.service.HelmAppService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.util.List;

/**
 * 我的应用
 */
@RestController
@RequestMapping("/helmApp")
@Api(value = "我的应用")
public class MineAppController extends BaseController {

    @Autowired
    private HelmAppService helmAppService;

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
        List<HelmApp> helmApps = helmAppService.list(new LambdaQueryWrapper<HelmApp>()
                .like(StrUtil.isNotBlank(appName), HelmApp::getAppName, appName)
                .like(StrUtil.isNotBlank(chartName), HelmApp::getChartName, chartName)
                .eq(StrUtil.isNotBlank(status), HelmApp::getStatus, status)
                .eq(HelmApp::getCreateBy, getUsername())
        );
        return AjaxResult.success(helmApps);
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
    public TableDataInfo page(Page<HelmApp> page,
                              @RequestParam(value = "appName", required = false) String appName,
                              @RequestParam(value = "chartName", required = false) String chartName,
                              @RequestParam(value = "status", required = false) String status) {
        // 获取分页参数
        PageUtils.toIPage(page);
        IPage<HelmApp> pageRes = helmAppService.page(page, new LambdaQueryWrapper<HelmApp>());
        return PageUtils.toPageByIPage(pageRes);
    }

}
