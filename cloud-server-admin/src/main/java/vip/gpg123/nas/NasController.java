package vip.gpg123.nas;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.NasFrpClientService;
import vip.gpg123.nas.service.NasFrpServerService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * nasOverView控制
 */
@RestController
@RequestMapping("/nas")
@Api(tags = "nasOverView控制")
public class NasController extends BaseController {

    @Autowired
    private NasFrpServerService nasFrpServerService;

    @Autowired
    private NasFrpClientService nasFrpClientService;

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "【概览】")
    public AjaxResult overview() {
        Map<String,Object> data = new LinkedHashMap<>();
        // 服务端数量
        data.put("frpServerTotalCount", nasFrpServerService.count());
        // 客户端数量
        data.put("frpClientTotalCount", nasFrpClientService.count(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getCreateBy, getUsername())
        ));
        // 正常数量
        data.put("frpClientOkCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getStatus, "ok")
                .eq(NasFrpClient::getCreateBy, getUsername())
        ).size());
        // 异常数量
        data.put("frpClientErrorCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getStatus, "error")
        ));
        // 在线数量
        data.put("frpClientOnlineCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getStatus, "online")
        ).size());
        // 离线
        data.put("frpClientOfflineCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getStatus, "offline")
        ).size());
        // http数量
        data.put("frpClientHttpCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getType, "http")
        ).size());
        // https数量
        data.put("frpClientHttpsCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getType, "https")
        ).size());
        // tcp数量
        data.put("frpClientTcpCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getType, "tcp")
        ).size());
        // udp数量
        data.put("frpClientUdpCount", nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getType, "udp")
        ));
        return AjaxResult.success(data);
    }
}
