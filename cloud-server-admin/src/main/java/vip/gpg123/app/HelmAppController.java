package vip.gpg123.app;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.app.domain.HelmApp;
import vip.gpg123.app.mapper.MineAppMapper;
import vip.gpg123.app.service.HelmAppService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.helm.HelmStatus;
import vip.gpg123.common.utils.helm.HelmUtils;

import java.util.List;

/**
 * 我的应用
 */
@RestController
@RequestMapping("/app/mine")
@Api(value = "我的应用")
public class HelmAppController extends BaseController {

    @Autowired
    private HelmAppService helmAppService;

    @Autowired
    private MineAppMapper mineAppMapper;

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
                .eq(HelmApp::getCreateBy, getUserId())
        );
        return AjaxResult.success(helmApps);
    }

    /**
     * 分页查询
     * @param appName 名称
     * @param chartName chart名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "appName", required = false) String appName,
                              @RequestParam(value = "chartName", required = false) String chartName,
                              @RequestParam(value = "status", required = false) String status) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<HelmApp> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        HelmApp search = new HelmApp();
        search.setAppName(appName);
        search.setChartName(chartName);
        search.setStatus(status);
        List<HelmApp> list = mineAppMapper.page(pageDomain, search);
        // 获取分页参数
        page.setRecords(list);
        page.setTotal(mineAppMapper.list(search).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 添加
     * @param helmApp app
     * @return  r
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加")
    public AjaxResult add(@RequestBody HelmApp helmApp) {
        // 添加
        boolean save = helmAppService.save(helmApp);
        return save ? success() : error();
    }

    /**
     * 修改
     * @param helmApp app
     * @return  r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody HelmApp helmApp) {
        // 修改
        boolean update = helmAppService.updateById(helmApp);
        return update ? success() : error();
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam(value = "id") Long id) {
        // 删除
        boolean remove = helmAppService.removeById(id);
        return remove ? success() : error();
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

}
