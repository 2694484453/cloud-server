package vip.gpg123.git.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * gitee api服务
 */
@FeignClient(name = "gitee-api", url = "https://gitee.com")
public interface GiteeApiService {

    /**
     * 获取仓库列表
     * @param access_token token
     * @param page 分页
     * @param per_page 分页
     * @param sort 排序
     * @param visibility 类型
     * @return r
     */
    @GetMapping("/api/v5/user/repos")
    List<?> repos(@RequestParam(value = "access_token") String access_token,
               @RequestParam(value = "page",  required = false, defaultValue = "1") String page,
               @RequestParam(value = "per_page",  required = false, defaultValue = "20") String per_page,
               @RequestParam(value = "sort", required = false, defaultValue = "full_name") String sort,
               @RequestParam(value = "visibility", required = false, defaultValue = "all") String visibility);


}
