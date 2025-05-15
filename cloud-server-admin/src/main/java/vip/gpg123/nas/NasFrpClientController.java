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
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.nas.domain.FrpServerHttp;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.FrpServerApiService;
import vip.gpg123.nas.service.NasFrpClientService;

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
    private FrpServerApiService frpServerApiService;

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
        IPage<NasFrpClient> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = nasFrpClientService.page(page, new LambdaQueryWrapper<NasFrpClient>()
                .like(StrUtil.isNotBlank(name), NasFrpClient::getName, name)
                .eq(StrUtil.isNotBlank(type), NasFrpClient::getType, type)
                .like(StrUtil.isNotBlank(ip), NasFrpClient::getLocalIp, ip)
                .like(ObjectUtil.isNotEmpty(port), NasFrpClient::getLocalPort, port)
                .eq(StrUtil.isNotBlank(server), NasFrpClient::getFrpServer, server)
                .eq(StrUtil.isNotBlank(getUsername()), NasFrpClient::getCreateBy, getUsername())
                .orderByDesc(NasFrpClient::getCreateTime));
        // 查询http代理
        String o = frpServerApiService.test();
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
        // 获取list
        List<NasFrpClient> list = page.getRecords();
        list.forEach(item -> {
            switch (item.getType()) {
                case "http":
                    if (httpMap.containsKey(item.getName())) {
                        item.setStatus(httpMap.get(item.getName()).getStatus());
                    }
                    break;
                case "https":
                    if (httpsMap.containsKey(item.getName())) {
                        item.setStatus(httpsMap.get(item.getName()).getStatus());
                    }
                    break;
                case "tcp":
                    if (tcpMap.containsKey(item.getName())) {
                        item.setStatus(tcpMap.get(item.getName()).getStatus());
                    }
                    break;
                case "udp":
                    if (udpMap.containsKey(item.getName())) {
                        item.setStatus(udpMap.get(item.getName()).getStatus());
                    }
                    break;
                default:
                    break;
            }
        });
        page.setRecords(list);
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
        if (StrUtil.isBlank(nasFrpClient.getName())) {
            return AjaxResult.error("名称不能为空");
        }
        long count = nasFrpClientService.count(new LambdaQueryWrapper<NasFrpClient>()
                .eq(NasFrpClient::getName, nasFrpClient.getName())
        );
        if (count > 1) {
            return AjaxResult.error("名称已经存在，请更换");
        }
        nasFrpClient.setCreateBy(getUsername());
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
        nasFrpClient.setUpdateBy(getUsername());
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
}
