package vip.gpg123.repo;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.repo.domain.HelmApp;
import vip.gpg123.repo.util.HelmUtils;

import java.util.List;

/**
 * @author gpg
 * @date 2025/4/20
 */
@RestController
@RequestMapping("/helmInstalled")
@Api(value = "Helm安装列表")
public class HelmInstalledController {

    /**
     * 列表查询
     * @param name name
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name",required = false) String name) {
        List<HelmApp> list = HelmUtils.list("", SecurityUtils.getUsername());
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param name name
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name",required = false) String name) {
        return PageUtils.toPage(ListUtil.toList(HelmUtils.list("", SecurityUtils.getUsername())));
    }
}
