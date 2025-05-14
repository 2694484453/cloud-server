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
        // 查询全部客户端
        List<NasFrpClient> list = nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getFrpServer, serverName)
                .eq(NasFrpClient::getCreateBy, getUsername())
                .orderByDesc(NasFrpClient::getCreateTime)
        );

        StringBuilder sb = new StringBuilder();
        // 配置服务端
        sb.append("#服务端配置(不可修改)").append("\n");
        sb.append("serverAddr = ").append("\"").append(nasFrpServer.getServerName()).append("\"").append("\n");
        sb.append("auth.token = ").append("\"").append(nasFrpServer.getAuthToken()).append("\"").append("\n");
        sb.append("#本地admin-ui(可修改)").append("\n");
        // 配置客户端
        sb.append("webServer.addr = ").append("\"").append("127.0.0.1").append("\"").append("\n");
        sb.append("webServer.port = ").append("\"").append("7500").append("\"").append("\n");
        sb.append("webServer.user = ").append("\"").append("admin").append("\"").append("\n");
        sb.append("webServer.password = ").append("\"").append("admin").append("\"").append("\n");
        // 配置代理
        sb.append("#代理配置(不可修改)").append("\n");
        list.forEach(nasFrpClient -> {
            sb.append("[[proxies]]").append("\n");
            sb.append("name = ").append("\"").append(nasFrpClient.getName()).append("\"").append("\n");
            sb.append("type = ").append("\"").append(nasFrpClient.getType()).append("\"").append("\n");
            sb.append("localIP = ").append("\"").append(nasFrpClient.getLocalIp()).append("\"").append("\n");
            sb.append("localPort = ").append("\"").append(nasFrpClient.getLocalPort()).append("\"").append("\n");
            sb.append("customDomains = ").append("[\"").append(nasFrpClient.getCustomDomains()).append("\"]").append("\n");
        });
        // 额外
        sb.append("healthCheck.type = ").append("\"").append("http").append("\"").append("\n");
        sb.append("healthCheck.path = ").append("\"").append("/status").append("\"").append("\n");
        sb.append("healthCheck.timeoutSeconds = ").append(3).append("\n");
        sb.append("healthCheck.maxFailed = ").append(3).append("\n");
        sb.append("healthCheck.intervalSeconds = ").append(10).append("\n");
        // 写入 TOML 文件
        String tempFilePath = FileUtil.getTmpDirPath() + File.separator + "config.toml";
        FileWriter fileWriter = new FileWriter(tempFilePath);
        fileWriter.write(sb.toString());
        fileWriter.close();
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
