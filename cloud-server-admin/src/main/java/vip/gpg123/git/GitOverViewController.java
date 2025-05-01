package vip.gpg123.git;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.git.domain.GitAccess;
import vip.gpg123.git.service.GitAccessService;

import java.util.HashMap;
import java.util.Map;

import static vip.gpg123.common.utils.SecurityUtils.getUsername;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: 概览
 * @date 2025/2/11 10:51
 */
@RestController
@RequestMapping("/git")
@Api(tags = "【git】概览")
public class GitOverViewController {

    @Autowired
    private GitAccessService gitAccessService;

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overview() {
        Map<String, Object> map = new HashMap<>();
        // 总配置数量
        map.put("totalAccessCount", gitAccessService.list(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getCreateBy, getUsername())
        ).size());
        // gitee  数量
        map.put("giteeAccessCount", gitAccessService.list(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getCreateBy, getUsername())
                .eq(GitAccess::getType, "gitee")
        ).size());
        // github  数量
        map.put("githubAccessCount", gitAccessService.list(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getCreateBy, getUsername())
                .eq(GitAccess::getType, "github")
        ).size());
        // gitlab  数量
        map.put("gitlabAccessCount", gitAccessService.list(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getCreateBy, getUsername())
                .eq(GitAccess::getType, "gitlab")
        ).size());
        // gitCode  数量
        map.put("gitCodeAccessCount", gitAccessService.list(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getCreateBy, getUsername())
                .eq(GitAccess::getType, "gitCode")
        ).size());
        return AjaxResult.success(map);
    }

}
