package vip.gpg123.nas;

import cn.hutool.core.util.ObjectUtil;
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
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.NasFrpClientService;

import java.util.List;

@RestController
@RequestMapping("/nas/frpc")
@Api(value = "nas-frpc管理")
public class NasFrpClientController extends BaseController {

    @Autowired
    private NasFrpClientService nasFrpClientService;

    /**
     * 列表查询
     *
     * @param name 名称
     * @param type 类型
     * @param ip   ip
     * @param port port
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    private AjaxResult list(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "type", required = false) String type,
                            @RequestParam(value = "ip", required = false) String ip,
                            @RequestParam(value = "port", required = false) Integer port,
                            @RequestParam(value = "server", required = false) String server) {
        List<NasFrpClient> list = nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .like(StrUtil.isNotBlank(name), NasFrpClient::getName, name)
                .eq(StrUtil.isNotBlank(type), NasFrpClient::getType, type)
                .like(StrUtil.isNotBlank(ip), NasFrpClient::getLocalIp, ip)
                .like(ObjectUtil.isNotEmpty(port), NasFrpClient::getLocalPort, port)
                .eq(StrUtil.isNotBlank(server), NasFrpClient::getFrpServer, server)
                .eq(StrUtil.isNotBlank(getUsername()), NasFrpClient::getCreateBy, getUsername())
                .orderByDesc(NasFrpClient::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param name 名称
     * @param type 类型
     * @param ip ip
     * @param port port
     * @param server server
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    private TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "ip", required = false) String ip,
                               @RequestParam(value = "port", required = false) Integer port,
                               @RequestParam(value = "server", required = false) String server) {
        IPage<NasFrpClient> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = nasFrpClientService.page(page, new LambdaQueryWrapper<NasFrpClient>()
                .like(StrUtil.isNotBlank(name), NasFrpClient::getName, name)
                .eq(StrUtil.isNotBlank(type), NasFrpClient::getType, type)
                .like(StrUtil.isNotBlank(ip), NasFrpClient::getLocalIp, ip)
                .like(ObjectUtil.isNotEmpty(port), NasFrpClient::getLocalPort, port)
                .eq(StrUtil.isNotBlank(server), NasFrpClient::getFrpServer, server)
                .eq(StrUtil.isNotBlank(getUsername()), NasFrpClient::getCreateBy, getUsername())
                .orderByDesc(NasFrpClient::getCreateTime));
        return PageUtils.toPageByIPage(page);
    }

}
