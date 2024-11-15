package com.ruoyi.web.controller.build;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.controller.build.domain.Git;
import com.ruoyi.web.controller.build.domain.GiteeRepo;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:48
 **/
@RestController
@RequestMapping("/git")
@Api(tags = "git仓库")
public class GitController {

    private static final String path = "/root/.my-server";

    /**
     * 列表查询
     *
     * @param type 类型
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam("type") String type) {
        List<Git> gitList = new ArrayList<>();
        String dirPath = path + "/git/" + SecurityUtils.getUsername();
        switch (type) {
            case "gitee":
                String filePath = dirPath + "/gitee-repo.json";
                gitList = giteeRepoList(filePath);
                break;
            case "github":
                break;
            case "gitlab":
                break;
            default:
                throw new RuntimeException("未知仓库");
        }
        return AjaxResult.success(gitList);
    }

    /**
     * 分页查询
     *
     * @param type 类型
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam("type") String type) {
        AjaxResult ajaxResult = list(type);
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }


    private List<Git> giteeRepoList(String filePath) {
        String content = FileUtil.readString(filePath, StandardCharsets.UTF_8);
        List<GiteeRepo> list = JSONUtil.toList(content, GiteeRepo.class);
        List<Git> gitList = new ArrayList<>();
        try (KubernetesClient client = K8sUtil.createKClient()) {
            list.forEach(e -> {
                // 填充
                Git git = new Git();
                git.setGitName(e.getName());
                git.setHome(e.getPath());
                git.setHttpUrl(e.getHtml_url());
                git.setSshUrl(e.getSsh_url());
                git.setGitId(e.getId());
                git.setLanguage(e.getLanguage());
                git.setType("gitee");
                // 是否含有job
                List<Job> jobs = client.batch().v1().jobs().inAnyNamespace().withLabel("app", e.getName()).list().getItems();
                git.setHasJob(ObjectUtil.isNotEmpty(jobs));
                git.setJobNumber(jobs.size());
                git.setType("");
                // 添加
                gitList.add(git);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gitList;
    }
}
