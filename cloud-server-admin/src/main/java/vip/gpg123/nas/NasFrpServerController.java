package vip.gpg123.nas;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
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
import vip.gpg123.nas.domain.NasFrpServer;
import vip.gpg123.nas.service.NasFrpServerService;

import java.util.List;

@RestController
@RequestMapping("/nas/frps")
@Api(value = "nas-frpc管理")
public class NasFrpServerController extends BaseController {

    @Autowired
    private NasFrpServerService nasFrpServerService;

    /**
     * 列表查询
     *
     * @param name 名称
     * @param ip   ip
     * @param port port
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    private AjaxResult list(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "ip", required = false) String ip,
                            @RequestParam(value = "port", required = false) Integer port) {
        List<NasFrpServer> list = nasFrpServerService.list(new LambdaQueryWrapper<NasFrpServer>()
                .like(StrUtil.isNotBlank(name), NasFrpServer::getServerName, name)
                .like(StrUtil.isNotBlank(ip), NasFrpServer::getServerIp, ip)
                .like(ObjectUtil.isNotEmpty(port), NasFrpServer::getServerPort, port)
                .eq(StrUtil.isNotBlank(getUsername()), NasFrpServer::getCreateBy, getUsername())
                .orderByDesc(NasFrpServer::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param name   名称
     * @param ip     ip
     * @param port   port
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    private TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "ip", required = false) String ip,
                               @RequestParam(value = "port", required = false) Integer port) {
        IPage<NasFrpServer> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = nasFrpServerService.page(page, new LambdaQueryWrapper<NasFrpServer>()
                .like(StrUtil.isNotBlank(name), NasFrpServer::getServerName, name)
                .like(StrUtil.isNotBlank(ip), NasFrpServer::getServerIp, ip)
                .like(ObjectUtil.isNotEmpty(port), NasFrpServer::getServerPort, port)
                .eq(StrUtil.isNotBlank(getUsername()), NasFrpServer::getCreateBy, getUsername())
                .orderByDesc(NasFrpServer::getCreateTime));
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 详情
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "【详情】")
    private AjaxResult info(@RequestParam(value = "id") String id) {
        NasFrpServer Server = nasFrpServerService.getById(id);
        return AjaxResult.success(Server);
    }

    /**
     * 新增
     *
     * @param nasFrpServer 客户端
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    private AjaxResult add(@RequestBody NasFrpServer nasFrpServer) {
        nasFrpServer.setCreateBy(getUsername());
        nasFrpServer.setCreateTime(DateUtil.date());
        boolean isSuccess = nasFrpServerService.save(nasFrpServer);
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     *
     * @param nasFrpServer 客户端
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "【修改】")
    private AjaxResult edit(@RequestBody NasFrpServer nasFrpServer) {
        nasFrpServer.setUpdateBy(getUsername());
        nasFrpServer.setUpdateTime(DateUtil.date());
        boolean isSuccess = nasFrpServerService.updateById(nasFrpServer);
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "【删除】")
    private AjaxResult delete(@RequestParam("id") String id) {
        boolean isSuccess = nasFrpServerService.removeById(id);
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }
}
