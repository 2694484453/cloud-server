package vip.gpg123.devops;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.PostMapping;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2024/9/1 16:06
 **/
@RestController
@RequestMapping("/build/helm")
public class HelmController {

    @Value("${build.helm}")
    private String helm;

    /**
     * 列表查询
     *
     * @param name n
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name) {
        File file = FileUtil.file(helm + "/" + "index.json");
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        return AjaxResult.success(jsonArray);
    }

    /**
     * 分页查询
     * @param name n
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name) {
        AjaxResult ajaxResult = list(name);
        JSONArray jsonArray = (JSONArray) ajaxResult.get("data");
        return PageUtils.toPage(jsonArray);
    }

    /**
     * 一键同步
     * @return r
     */
    @PostMapping("/syncPull")
    @ApiOperation(value = "同步")
    public AjaxResult sync(){
        // 你的本地仓库路径
        File repoPath = new File(helm);
        try {
            // 打开现有的仓库
            Git git = Git.open(repoPath);
            // 调用pull()方法来拉取最新的更改
            PullResult result = git.pull().call();
            // 输出pull操作的一些信息
            System.out.println("Pull result: " + result.toString());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success("同步成功");
    }
}
