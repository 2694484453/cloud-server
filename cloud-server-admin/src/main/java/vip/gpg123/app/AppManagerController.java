package vip.gpg123.app;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.service.AppService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.common.utils.helm.HelmApp;
import vip.gpg123.common.utils.helm.HelmStatus;
import vip.gpg123.common.utils.helm.HelmUtils;

import java.util.List;

/**
 * @author gpg
 * @date 2025/4/20
 */
@RestController
@RequestMapping("/helm")
@Api(value = "Helm安装列表")
public class AppManagerController extends BaseController {

    @Value("${repo.helm.url}")
    private String url;

    @Value("${repo.helm.name}")
    private String repoName;

    @Autowired
    private AppService appService;

    /**
     * 列表查询
     *
     * @param name name
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name) {
        List<HelmApp> list = HelmUtils.list("", SecurityUtils.getUsername());
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param name name
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name) {
        return PageUtils.toPage(ListUtil.toList(HelmUtils.list("", SecurityUtils.getUsername())));
    }

    /**
     * 安装
     *
     * @param mineApp app
     * @return r
     */
    @PostMapping("/install")
    @ApiOperation(value = "安装")
    public AjaxResult install(@RequestBody MineApp mineApp) {
        appService.install(mineApp.getReleaseName(), mineApp.getNameSpace(), mineApp.getChartUrl(), mineApp.getValue(), mineApp.getKubeContext());
        return AjaxResult.success("安装成功");
    }

    /**
     * 查询详情
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情")
    public AjaxResult info(@RequestParam(value = "name") String name) {
        try {
            // 查询详情
            HelmStatus status = HelmUtils.status(name, name, getUsername());
            return AjaxResult.success(status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 管理
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/manager")
    @ApiOperation(value = "查询")
    public AjaxResult manager(@RequestParam(value = "name") String name) {
        // 查询工作负载

        return AjaxResult.success(HelmUtils.list("", getUsername()));
    }
}
