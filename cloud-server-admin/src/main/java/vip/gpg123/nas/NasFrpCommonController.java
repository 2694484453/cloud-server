package vip.gpg123.nas;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.moandjiezana.toml.TomlWriter;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *
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
     *
     * @param serverName 服务
     * @param params     p
     */
    @PostMapping("/export")
    @ApiOperation(value = "【导出配置文件】")
    public void export(@RequestParam(value = "serverName", required = false, defaultValue = "hcs.gpg123.vip") String serverName,
                             @RequestBody Map<String, Object> params,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        // 查询服务
        NasFrpServer nasFrpServer = nasFrpServerService.getOne(new LambdaQueryWrapper<NasFrpServer>()
                .eq(NasFrpServer::getServerName, serverName)
        );
        if (ObjectUtil.isNull(nasFrpServer)) {
            throw new RuntimeException("服务不存在");
        }
        List<NasFrpClient> list;
        // 导出全部
        list = nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getFrpServer, serverName)
                .eq(NasFrpClient::getCreateBy, getUsername())
                .orderByDesc(NasFrpClient::getCreateTime)
        );
        // 配置服务端
        Setting setting = new Setting();
        setting.set("serverAddr", nasFrpServer.getServerName());
        setting.set("serverPort", nasFrpServer.getServerPort().toString());
        setting.entrySet("auth");
        setting.set("token", nasFrpServer.getAuthToken());
        // client-admin-ui
        setting.entrySet("webServer");
        setting.set("webServer.addr", "127.0.0.1");
        setting.set("webServer.port", "8081");
        setting.set("webServer.user", "admin");
        setting.set("webServer.password", "admin");
        // proxies
        for (NasFrpClient nasFrpClient : list) {
            setting.entrySet("[[proxies]]");
            setting.set("name", nasFrpClient.getName());
            setting.set("type", nasFrpClient.getType());
            setting.set("localIp", nasFrpClient.getLocalIp());
            setting.set("localPort", nasFrpClient.getLocalPort().toString());
            setting.set("customDomains", nasFrpClient.getCustomDomains());
        }
        setting.set("healthCheck.type", "http");
        setting.set("healthCheck.path", "/status");
        setting.set("healthCheck.timeoutSeconds", "3");
        setting.set("healthCheck.maxFailed", "3");
        setting.set("healthCheck.intervalSeconds", "10");
        // 转换为 Map 结构
        Map<String, Object> configMap = new HashMap<>();
        setting.entrySet().forEach(entry -> {
            configMap.put(entry.getKey(), entry.getValue());
        });

        // 写入 TOML 文件
        String tempFilePath = FileUtil.getTmpDirPath() + File.separator + "config.toml";
        new TomlWriter().write(setting, new File(tempFilePath));
        try {
            File file = FileUtil.file(tempFilePath);
            // 1. 设置响应头
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("frpc.toml", "UTF-8"));
            // 2. 带缓冲的流复制
            try (InputStream in = new BufferedInputStream(FileUtil.getInputStream(file));
                 OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
                IoUtil.copy(in, out);
                IoUtil.close(in);
                IoUtil.close(out);
            }
        } catch (Exception e) {
            response.sendError(500, "文件导出失败：" + e.getMessage());
        }
    }
}
