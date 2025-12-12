package vip.gpg123.app;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.mapper.MineAppMapper;
import vip.gpg123.app.service.MineAppService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.quartz.domain.SysJob;

import java.util.List;

/**
 * 我的应用
 */
@RestController
@RequestMapping("/app/mine")
@Api(value = "我的应用")
public class MineAppController extends BaseController {

    @Autowired
    private MineAppService helmAppService;

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
        List<MineApp> mineApps = helmAppService.list(new LambdaQueryWrapper<MineApp>()
                .like(StrUtil.isNotBlank(appName), MineApp::getAppName, appName)
                .like(StrUtil.isNotBlank(chartName), MineApp::getChartName, chartName)
                .eq(StrUtil.isNotBlank(status), MineApp::getStatus, status)
                .eq(MineApp::getCreateBy, getUserId())
        );
        return AjaxResult.success(mineApps);
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
        IPage<MineApp> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        MineApp search = new MineApp();
        search.setAppName(appName);
        search.setChartName(chartName);
        search.setStatus(status);
        List<MineApp> list = mineAppMapper.page(pageDomain, search);
        // 获取分页参数
        page.setRecords(list);
        page.setTotal(mineAppMapper.list(search).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 添加
     * @param mineApp app
     * @return  r
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加")
    public AjaxResult add(@RequestBody MineApp mineApp) {
        // 添加
        boolean save = helmAppService.save(mineApp);
        return save ? success() : error();
    }

    /**
     * 修改
     * @param mineApp app
     * @return  r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody MineApp mineApp) {
        // 修改
        boolean update = helmAppService.updateById(mineApp);
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
}
