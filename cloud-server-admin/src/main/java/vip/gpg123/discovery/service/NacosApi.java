package vip.gpg123.discovery.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.discovery.vo.NacosConfigResponse;
import vip.gpg123.discovery.vo.NacosNameSpaceResponse;
import vip.gpg123.discovery.vo.NacosServiceResponse;
import vip.gpg123.discovery.vo.NameSpaceItem;

import java.util.Map;

@FeignClient(name = "nacos-api", url = "http://hcs.gpg123.vip:8848")
@Service
public interface NacosApi {

    /**
     * 指标
     * @return r
     */
    @GetMapping("/nacos/v1/ns/operator/metrics")
    Map<String,Object> metrics();

    /**
     * 所有服务
     * @return r
     */
    @GetMapping("/nacos/v1/ns/service/list")
    NacosServiceResponse services();

    /**
     * 获取命名空间列表
     * @return r
     */
    @GetMapping("/nacos/v1/console/namespaces")
    NacosNameSpaceResponse<NameSpaceItem> namespaces();

    /**
     * 获取服务列表
     * @param namespaceId id
     * @param pageNo 分页
     * @param pageSize 分页
     * @return r
     */
    @GetMapping("/nacos/v1/ns/service/list")
    NacosServiceResponse service(@RequestParam(value = "namespaceId") String namespaceId,
                                 @RequestParam(value = "pageNo",required = false, defaultValue = "1") Integer pageNo,
                                 @RequestParam(value = "pageSize",required = false, defaultValue = "10") Integer pageSize);

    /**
     * 创建ns
     * @param params 参数
     * @return r
     */
    @PostMapping("/nacos/v1/console/namespaces")
    Boolean createNs(@RequestBody Map<String, String> params);


    /**
     * 删除ns
     * @param namespaceId id
     * @return r
     */
    @DeleteMapping("/nacos/v1/console/namespaces")
    Boolean deleteNs(@RequestParam(name = "namespaceId") String namespaceId);

    /**
     * 获取配置列表
     * @param dataId id
     * @param pageNo 分页
     * @param pageSize 分页
     * @param name 配置名称
     * @return r
     */
    @GetMapping("/nacos/v1/cs/configs")
    NacosConfigResponse configs(@RequestParam(name = "dataId", required = false, defaultValue = "") String dataId,
                                @RequestParam(name = "group",required = false, defaultValue = "") String group,
                                @RequestParam(name = "appName",required = false, defaultValue = "") String appName,
                                @RequestParam(name = "tenant",required = false, defaultValue = "") String tenant,
                                @RequestParam(name = "pageNo",required = false, defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize",required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(name = "name",required = false, defaultValue = "") String name);
}
