package vip.gpg123.git.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import vip.gpg123.git.domain.GitlabRepo;

import java.util.List;

@FeignClient(name = "gitlab-api", url = "https://jihulab.com")
@Service
public interface GitlabApiService {

    /**
     * 获取项目列表
     *
     * @param token   token
     * @param page    分页
     * @param perPage 分页
     * @param orderBy 排序
     * @param sort    排序
     * @return r
     */
    @GetMapping(value = "/api/v4/projects", produces = "application/json")
    List<GitlabRepo> listProjects(@RequestHeader("PRIVATE-TOKEN") String token,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                                  @RequestParam(value = "per_page", required = false, defaultValue = "20") String perPage,
                                  @RequestParam(value = "order_by", required = false, defaultValue = "full_name") String orderBy,
                                  @RequestParam(value = "sort", required = false, defaultValue = "full_name") String sort);
}