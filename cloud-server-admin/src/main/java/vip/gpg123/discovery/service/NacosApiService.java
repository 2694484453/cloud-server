package vip.gpg123.discovery.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.discovery.domain.NacosNameSpace;
import vip.gpg123.discovery.domain.NacosResponse;
import vip.gpg123.discovery.domain.NacosService;

import java.util.Map;

@FeignClient(name = "nacos-api", url = "http://hcs.gpg123.vip:8848")
@Service
public interface NacosApiService {

    @GetMapping("/nacos/v1/ns/service/list")
    NacosService services();

    /**
     * 获取命名空间列表
     * @return r
     */
    @GetMapping("/nacos/v1/console/namespaces")
    NacosResponse<NacosNameSpace> namespaces();

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
}
