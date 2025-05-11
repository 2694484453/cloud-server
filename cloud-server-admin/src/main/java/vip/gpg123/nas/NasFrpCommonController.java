package vip.gpg123.nas;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.moandjiezana.toml.Toml;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.domain.NasFrpServer;
import vip.gpg123.nas.service.NasFrpClientService;
import vip.gpg123.nas.service.NasFrpServerService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/nas/frp/common")
@Api(value = "nas-frp通用管理")
public class NasFrpCommonController extends BaseController {

    @Autowired
    private NasFrpClientService nasFrpClientService;

    @Autowired
    private NasFrpServerService nasFrpServerService;

    /**
     * 类型查询
     * @return r
     */
    @GetMapping("/types")
    @ApiOperation(value = "【类型查询】")
    public AjaxResult types() {
        List<String> types = new ArrayList<>();
        types.add("http");
        types.add("https");
        types.add("tcp");
        types.add("udp");
        types.add("stcp");
        types.add("sudp");
        types.add("tcpmux");
        return AjaxResult.success(types);
    }

    /**
     * 导出配置文件
     * @param serverName 服务
     * @param ids ids
     * @return r
     */
    @PostMapping("/export")
    @ApiOperation(value = "【导出配置文件】")
    public AjaxResult export(@RequestParam(value = "serverName") String serverName,
                             @RequestBody List<String> ids) {
        // 查询服务
        NasFrpServer nasFrpServer = nasFrpServerService.getOne(new LambdaQueryWrapper<NasFrpServer>()
                .eq(NasFrpServer::getServerName, serverName)
        );
        if (ObjectUtil.isNull(nasFrpServer)) {
            return AjaxResult.error("服务不存在");
        }
        List<NasFrpClient> list = new ArrayList<>();
        // 导出所选
        if (ObjectUtil.isNotEmpty(ids)) {
            list = nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                    .eq(NasFrpClient::getFrpServer, serverName)
                    .eq(NasFrpClient::getCreateBy, getUsername())
                    .in(NasFrpClient::getId, ids)
                    .orderByDesc(NasFrpClient::getCreateTime)
            );
        }else {
            // 导出全部
            list = nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                    .eq(NasFrpClient::getFrpServer, serverName)
                    .eq(NasFrpClient::getCreateBy, getUsername())
                    .orderByDesc(NasFrpClient::getCreateTime)
            );
        }
        // 配置服务端
        Toml toml = new Toml();

        return AjaxResult.success();
    }
}
