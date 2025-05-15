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
import vip.gpg123.nas.domain.FrpServerHttp;
import vip.gpg123.nas.domain.FrpServerInfo;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.FrpServerApiService;
import vip.gpg123.nas.service.NasFrpClientService;
import vip.gpg123.nas.service.NasFrpServerService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private FrpServerApiService frpServerApiService;

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "【概览】")
    public AjaxResult overview() {
        // 查询服务端
        FrpServerInfo frpServerInfo = frpServerApiService.serverInfo();
        // 查询http代理
        List<FrpServerHttp> httpList = frpServerApiService.httpList().getProxies();
        Map<String, FrpServerHttp> httpMap = httpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询https代理
        List<FrpServerHttp> httpsList = frpServerApiService.httpsList().getProxies();
        Map<String, FrpServerHttp> httpsMap = httpsList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询tcp代理
        List<FrpServerHttp> tcpList = frpServerApiService.tcpList().getProxies();
        Map<String, FrpServerHttp> tcpMap = tcpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询udp代理
        List<FrpServerHttp> udpList = frpServerApiService.udpList().getProxies();
        Map<String, FrpServerHttp> udpMap = udpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        Map<String,Object> data = new LinkedHashMap<>();
        // 服务端信息
        data.put("serverInfo",frpServerInfo);
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
        data.put("frpClientHttpCount", httpList.size());
        // https数量
        data.put("frpClientHttpsCount", httpsList.size());
        // tcp数量
        data.put("frpClientTcpCount", tcpList.size());
        // udp数量
        data.put("frpClientUdpCount", udpList.size());
        return AjaxResult.success(data);
    }
}
