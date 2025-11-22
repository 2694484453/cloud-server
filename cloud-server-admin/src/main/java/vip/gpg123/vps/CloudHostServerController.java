package vip.gpg123.vps;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.Session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.vps.domain.CloudHostServer;
import vip.gpg123.vps.mapper.CloudHostServerMapper;
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

    @Autowired
    private CloudHostServerMapper cloudHostServerMapper;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "hostName",required = false) String hostName,
                           @RequestParam(value = "type",required = false) String type) {
        List<CloudHostServer> serversList = cloudHostServerService.list(new LambdaQueryWrapper<CloudHostServer>()
                .eq(CloudHostServer::getCreateBy,  getUsername())
                .like(StrUtil.isNotBlank(hostName), CloudHostServer::getHostName, hostName)
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
    public TableDataInfo page(@RequestParam(value = "hostName",required = false) String hostName,
                              @RequestParam(value = "type",required = false) String type) {

        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<CloudHostServer> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        CloudHostServer search = new CloudHostServer();
        search.setHostName(hostName);
        search.setHostType(type);
        search.setCreateBy(String.valueOf(getUserId()));
        List<CloudHostServer> list = cloudHostServerMapper.page(pageDomain, search);
        page.setRecords(list);
        page.setTotal(cloudHostServerMapper.selectList(search).size());
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
    @PostMapping("/add")
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

    /**
     * 检查连接
     * @param cloudHostServer cloudHostServer
     * @return r
     */
    @PostMapping("/checkConnect")
    @ApiOperation(value = "检查连接")
    public AjaxResult checkConnect(@RequestBody CloudHostServer cloudHostServer) {
        try {
            Session session = JschUtil.createSession(cloudHostServer.getHostIp(), cloudHostServer.getPort(), cloudHostServer.getUserName(), cloudHostServer.getPassWord());
            ThreadUtil.sleep(3000);
            if (ObjectUtil.isNull(session)) {
                return AjaxResult.error("连接失败");
            }
            session.disconnect();
            return AjaxResult.success("连接成功");
        } catch (Exception e) {
            return AjaxResult.error("连接失败");
        }
    }
}
