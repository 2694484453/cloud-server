package vip.gpg123.git.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.gpg123.git.domain.GithubRepo;

import java.util.List;

/**
 * github-api
 */
@FeignClient(name = "github-api", url = "https://api.github.com")
@Service
public interface GithubApiService {

    /**
     * 获取仓库列表
     * @param access_token token
     * @param page 页码
     * @param per_page 分页
     * @param sort 排序
     * @param visibility 可见性
     * @return r
     */
    @GetMapping("/user/repos")
    List<GithubRepo> repos(@RequestParam(value = "access_token") String access_token,
                           @RequestParam(value = "page",  required = false, defaultValue = "1") String page,
                           @RequestParam(value = "per_page",  required = false, defaultValue = "20") String per_page,
                           @RequestParam(value = "sort", required = false, defaultValue = "created") String sort,
                           @RequestParam(value = "visibility", required = false, defaultValue = "all") String visibility);
}
