package vip.gpg123.discovery.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.discovery.vo.NacosNameSpaceResponse;
import vip.gpg123.discovery.vo.NacosServiceResponse;
import vip.gpg123.discovery.vo.NameSpace;

import java.util.Map;

@FeignClient(name = "nacos-api", url = "http://hcs.gpg123.vip:8848")
@Service
public interface NacosApiService {

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
    NacosNameSpaceResponse<NameSpace> namespaces();

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
    Boolean deleteNs(@RequestParam(value = "namespaceId") String namespaceId);

    /**
     * 获取配置列表
     * @param dataId id
     * @param pageNo 分页
     * @param pageSize 分页
     * @param name 配置名称
     * @return r
     */
    @GetMapping("/nacos/v1/cs/configs")
    NacosApiService configs(@RequestParam(value = "dataId", required = false) String dataId,
                         @RequestParam(value = "group",required = false) String group,
                         @RequestParam(value = "appName",required = false) String appName,
                         @RequestParam(value = "tenant",required = false) String tenant,
                         @RequestParam(value = "pageNo",required = false, defaultValue = "1") Integer pageNo,
                         @RequestParam(value = "pageSize",required = false, defaultValue = "10") Integer pageSize,
                         @RequestParam(value = "name",required = false) String name);
}
