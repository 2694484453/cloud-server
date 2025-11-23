package vip.gpg123.nas;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.nas.domain.FrpServerHttp;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.mapper.NasFrpClientMapper;
import vip.gpg123.nas.service.FrpServerApiService;
import vip.gpg123.nas.service.NasFrpClientService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nas/frpc")
@Api(value = "nas-frpc管理")
public class NasFrpClientController extends BaseController {

    @Autowired
    private NasFrpClientService nasFrpClientService;

    @Autowired
    private NasFrpClientMapper nasFrpClientMapper;

    @Autowired
    private FrpServerApiService frpServerApiService;

    @Value("${frp.server.ip}")
    private String host;

    @Value("${frp.server.port}")
    private String port;

    @Value("${frp.server.token}")
    private String token;

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
                .eq(StrUtil.isNotBlank(getUsername()), NasFrpClient::getCreateBy, getUsername())
                .orderByDesc(NasFrpClient::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param name   名称
     * @param type   类型
     * @param ip     ip
     * @param port   port
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
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<NasFrpClient> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        //
        NasFrpClient search = new NasFrpClient();
        search.setName(name);
        search.setType(type);
        search.setLocalIp(ip);
        search.setCreateBy(String.valueOf(getUserId()));
        List<NasFrpClient> list = nasFrpClientMapper.page(pageDomain,search);
        page.setRecords(list);
        page.setTotal(nasFrpClientMapper.list(search).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 详情
     *
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "【详情】")
    private AjaxResult info(@RequestParam(value = "id") String id) {
        NasFrpClient client = nasFrpClientService.getById(id);
        return AjaxResult.success(client);
    }

    /**
     * 新增
     *
     * @param nasFrpClient 客户端
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    private AjaxResult add(@RequestBody NasFrpClient nasFrpClient) {
        if (StrUtil.isBlankIfStr(nasFrpClient.getName())) {
            return AjaxResult.error("名称不能为空");
        }
        long count = nasFrpClientService.count(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getName, nasFrpClient.getName())
        );
        if (count > 0) {
            return AjaxResult.error("名称已经存在，请更换");
        }
        nasFrpClient.setCreateBy(String.valueOf(getUserId()));
        nasFrpClient.setCreateTime(DateUtil.date());
        boolean isSuccess = nasFrpClientService.save(nasFrpClient);
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     *
     * @param nasFrpClient 客户端
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "【修改】")
    private AjaxResult edit(@RequestBody NasFrpClient nasFrpClient) {
        if (StrUtil.isBlank(nasFrpClient.getId())) {
            return AjaxResult.error("id不能为空");
        }
        // 查询旧数据
        NasFrpClient old = nasFrpClientService.getById(nasFrpClient.getId());
        // 判断是否修改
        if (!old.getName().equals(nasFrpClient.getName())) {
            // 如果修改了名称，需要判断是否与其他服务冲突
            long count = nasFrpClientService.count(new LambdaQueryWrapper<NasFrpClient>()
                    .eq(NasFrpClient::getName, nasFrpClient.getName())
            );
            if (count > 0) {
                return AjaxResult.error("名称已被占用，请更换");
            }
        }
        nasFrpClient.setUpdateBy(String.valueOf(getUserId()));
        nasFrpClient.setUpdateTime(DateUtil.date());
        boolean isSuccess = nasFrpClientService.updateById(nasFrpClient);
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除
     *
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "【删除】")
    private AjaxResult delete(@RequestParam("id") String id) {
        // 查询是否存在
        NasFrpClient client = nasFrpClientService.getById(id);
        if (client == null) {
            return AjaxResult.error("id不存在");
        }
        boolean isSuccess = nasFrpClientService.removeById(id);
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 导出配置文件
     */
    @PostMapping("/export")
    @ApiOperation(value = "【导出配置文件】")
    public void export(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        // 查询全部客户端
        List<NasFrpClient> list = nasFrpClientService.list(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getCreateBy, getUserId())
                .orderByDesc(NasFrpClient::getCreateTime)
        );

        StringBuilder sb = new StringBuilder();
        // 配置服务端
        sb.append("#服务端配置(不可修改)").append("\n");
        sb.append("serverAddr = ").append("\"").append(host).append("\"").append("\n");
        sb.append("serverPort = ").append("\"").append(port).append("\"").append("\n");
        sb.append("auth.token = ").append("\"").append(token).append("\"").append("\n");
        sb.append("#本地admin-ui(可修改)").append("\n");
        // 配置客户端
        sb.append("webServer.addr = ").append("\"").append("127.0.0.1").append("\"").append("\n");
        sb.append("webServer.port = ").append("\"").append("7500").append("\"").append("\n");
        sb.append("webServer.user = ").append("\"").append("admin").append("\"").append("\n");
        sb.append("webServer.password = ").append("\"").append("admin").append("\"").append("\n");
        // 配置代理
        sb.append("#代理配置(不可修改)").append("\n");
        list.forEach(nasFrpClient -> {
            sb.append("#").append(nasFrpClient.getName()).append(",备注：").append(nasFrpClient.getDescription()).append("\n");
            sb.append("[[proxies]]").append("\n");
            sb.append("name = ").append("\"").append(nasFrpClient.getName()).append("\"").append("\n");
            sb.append("type = ").append("\"").append(nasFrpClient.getType()).append("\"").append("\n");
            sb.append("localIP = ").append("\"").append(nasFrpClient.getLocalIp()).append("\"").append("\n");
            sb.append("localPort = ").append("\"").append(nasFrpClient.getLocalPort()).append("\"").append("\n");
            sb.append("customDomains = ").append("[\"").append(nasFrpClient.getCustomDomains()).append("\"]").append("\n");
        });
        // 额外
        sb.append("#其他(可修改)").append("\n");
        sb.append("healthCheck.type = ").append("\"").append("http").append("\"").append("\n");
        sb.append("healthCheck.path = ").append("\"").append("/status").append("\"").append("\n");
        sb.append("healthCheck.timeoutSeconds = ").append(3).append("\n");
        sb.append("healthCheck.maxFailed = ").append(3).append("\n");
        sb.append("healthCheck.intervalSeconds = ").append(10).append("\n");
        sb.append("#注意：如果要使用域名访问请设置您的域名为cName类型并解析到frp.gpg123.vip").append("\n");
        try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            // 1. 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("frpc.toml", "UTF-8"));
            // 2. 带缓冲的流复制
            IoUtil.copy(IoUtil.toUtf8Stream(sb.toString()), out);
        } catch (Exception e) {
            logger.error("文件导出失败：{}", e.getMessage());
            response.sendError(500, "文件导出失败：" + e.getMessage());
        }
    }

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "【概览】")
    public AjaxResult overview() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> clientCount = new LinkedHashMap<>();
        // 我的frpc配置总数
        clientCount.put("title","我的frpc配置总数");
        clientCount.put("count",nasFrpClientService.count(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getCreateBy, getUserId())
        ));
        // 我的frpc配置在线数
        Map<String,Object> healthClientCount = new LinkedHashMap<>();
        healthClientCount.put("title","我的frpc配置在线数");
        healthClientCount.put("count", nasFrpClientService.count(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getStatus, "online")
                .eq(NasFrpClient::getCreateBy, getUserId())
        ));

        // 我的frpc配置离线数
        Map<String,Object> downClientCount = new LinkedHashMap<>();
        downClientCount.put("title","我的frpc配置离线数");
        downClientCount.put("count", nasFrpClientService.count(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getCreateBy, getUserId())
                .eq(NasFrpClient::getStatus, "error")
        ));
        list.add(clientCount);
        list.add(healthClientCount);
        list.add(downClientCount);
        return AjaxResult.success(list);
    }

    /**
     * 同步状态
     * @return r
     */
    @GetMapping("/sync")
    public AjaxResult sync() {
        // 查询http代理
        String o = frpServerApiService.test();
        List<FrpServerHttp> httpList = frpServerApiService.httpList().getProxies();
        Map<String, FrpServerHttp> httpMap = httpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 更新

        // 查询https代理
        List<FrpServerHttp> httpsList = frpServerApiService.httpsList().getProxies();
        Map<String, FrpServerHttp> httpsMap = httpsList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询tcp代理
        List<FrpServerHttp> tcpList = frpServerApiService.tcpList().getProxies();
        Map<String, FrpServerHttp> tcpMap = tcpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询udp代理
        List<FrpServerHttp> udpList = frpServerApiService.udpList().getProxies();
        Map<String, FrpServerHttp> udpMap = udpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 获取list
        List<NasFrpClient> list = nasFrpClientService.list();
        list.forEach(item -> {
            switch (item.getType()) {
                case "http":
                    if (httpMap.containsKey(item.getName())) {
                        item.setStatus(httpMap.get(item.getName()).getStatus());
                    } else {
                        item.setStatus("noExist");
                    }
                    break;
                case "https":
                    if (httpsMap.containsKey(item.getName())) {
                        item.setStatus(httpsMap.get(item.getName()).getStatus());
                    }else {
                        item.setStatus("noExist");
                    }
                    break;
                case "tcp":
                    if (tcpMap.containsKey(item.getName())) {
                        item.setStatus(tcpMap.get(item.getName()).getStatus());
                    }else {
                        item.setStatus("noExist");
                    }
                    break;
                case "udp":
                    if (udpMap.containsKey(item.getName())) {
                        item.setStatus(udpMap.get(item.getName()).getStatus());
                    }else {
                        item.setStatus("noExist");
                    }
                    break;
                default:
                    break;
            }
            nasFrpClientService.updateById(item);
        });
        return AjaxResult.success();
    }
}
