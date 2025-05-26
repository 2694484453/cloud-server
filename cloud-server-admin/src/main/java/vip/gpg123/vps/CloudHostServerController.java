package vip.gpg123.vps;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.vps.domain.CloudHostServer;
import vip.gpg123.vps.service.CloudHostServerService;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/6 1:54
 **/
@RestController
@RequestMapping("/cloud-host")
@Api(tags = "云主机")
public class CloudHostServerController extends BaseController {

    @Autowired
    private CloudHostServerService cloudHostServerService;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name",required = false) String name,
                           @RequestParam(value = "type",required = false) String type) {
        List<CloudHostServer> serversList = cloudHostServerService.list(new LambdaQueryWrapper<CloudHostServer>()
                .eq(CloudHostServer::getCreateBy,  getUsername())
                .like(StrUtil.isNotBlank(name), CloudHostServer::getHostName, name)
                .eq(StrUtil.isNotBlank(type), CloudHostServer::getHostType, type)
                .orderByDesc(CloudHostServer::getCreateTime)
        );
        return AjaxResult.success(serversList);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name",required = false) String name,
                              @RequestParam(value = "type",required = false) String type) {
        IPage<CloudHostServer> page = cloudHostServerService.page(new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize()), new LambdaQueryWrapper<CloudHostServer>()
                .eq(CloudHostServer::getCreateBy,  getUsername())
                .like(StrUtil.isNotBlank(name), CloudHostServer::getHostName, name)
                .eq(StrUtil.isNotBlank(type), CloudHostServer::getHostType, type)
                .orderByDesc(CloudHostServer::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 详情查询
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam(value = "id",required = false) String id) {
        CloudHostServer cloudHostServer = cloudHostServerService.getById(id);
        return AjaxResult.success(cloudHostServer);
    }

    /**
     * 新增
     * @param cloudHostServer cloudHostServer
     * @return r
     */
    @GetMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody CloudHostServer cloudHostServer) {
        cloudHostServer.setCreateBy(getUsername());
        cloudHostServer.setCreateTime(DateUtil.date());
        // 对密码特殊处理
        boolean save = cloudHostServerService.save(cloudHostServer);
        return save ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     * @param cloudHostServer cloudHostServer
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody CloudHostServer cloudHostServer) {
        cloudHostServer.setUpdateBy(getUsername());
        cloudHostServer.setUpdateTime(DateUtil.date());
        boolean update = cloudHostServerService.updateById(cloudHostServer);
        return update ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam(value = "id",required = false) String id) {
        boolean remove = cloudHostServerService.removeById(id);
        if (remove) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
