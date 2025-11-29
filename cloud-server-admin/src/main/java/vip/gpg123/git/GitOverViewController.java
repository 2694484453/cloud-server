package vip.gpg123.git;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.git.domain.GitToken;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.service.GitTokenService;
import vip.gpg123.git.service.GitRepoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: 概览
 * @date 2025/2/11 10:51
 */
@RestController
@RequestMapping("/git")
@Api(tags = "【git】概览")
public class GitOverViewController extends BaseController {

    @Autowired
    private GitTokenService gitTokenService;

    @Autowired
    private GitRepoService gitRepoService;

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overview() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        // 总配置数量
        map.put("title", "我的token配置总数量");
        map.put("count", gitTokenService.list(new LambdaQueryWrapper<GitToken>()
                .eq(GitToken::getCreateBy, getUserId())
        ).size());
        list.add(map);

        // gitee 数量
        Map<String, Object> map2 = new HashMap<>();
        map2.put("title", "我的仓库总数量");
        map2.put("count", gitRepoService.count(new LambdaQueryWrapper<GitRepo>()
                .eq(GitRepo::getCreateBy, getUserId())
        ));
        list.add(map2);

        // github  数量
        Map<String, Object> map3 = new HashMap<>();
        map3.put("title", "github仓库数量");
        map3.put("count", gitRepoService.count(new LambdaQueryWrapper<GitRepo>()
                .eq(GitRepo::getCreateBy, getUserId())
                .eq(GitRepo::getType, "github")
        ));
        list.add(map3);

        // gitlab  数量
        Map<String, Object> map4 = new HashMap<>();
        map4.put("title", "gitlab仓库数量");
        map4.put("count", gitRepoService.count(new LambdaQueryWrapper<GitRepo>()
                .eq(GitRepo::getCreateBy, getUserId())
                .eq(GitRepo::getType, "gitlab")
        ));
        list.add(map4);

        return AjaxResult.success(list);
    }

}
