package vip.gpg123.git;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.git.domain.GitAccess;
import vip.gpg123.git.service.GitAccessService;
import vip.gpg123.git.service.GiteeApiService;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/16 0:08
 **/
@RestController
@RequestMapping("/gitRepo")
@Api(tags = "【git】仓库管理")
public class GitRepoController extends BaseController {

    @Autowired
    private GitAccessService gitAccessService;

    @Autowired
    private GiteeApiService giteeApiService;


    /**
     * 获取type
     *
     * @return r
     */
    @GetMapping("/types")
    @ApiOperation(value = "获取type")
    public AjaxResult token() {
        List<String> types = new ArrayList<>();
        types.add("gitee");
        types.add("github");
        types.add("gitlab");
        types.add("github");
        types.add("gitcode");
        return AjaxResult.success(types);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo repos(@RequestParam(value = "type") String type) {
        // 获取token
        GitAccess gitAccess = gitAccessService.getOne(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getType, type)
                .eq(GitAccess::getCreateBy, getUsername())
        );
        if (gitAccess == null) {
            throw new RuntimeException("请先添加"+ type +"类型认证");
        }

        String accessToken = gitAccess.getAccessToken();
        Integer pageNum = TableSupport.buildPageRequest().getPageNum();
        Integer pageSize = TableSupport.buildPageRequest().getPageSize();
        //
        List<?> repoList = new ArrayList<>();
        switch (type) {
            case "gitee":
                repoList = giteeApiService.repos(accessToken, String.valueOf(pageNum), String.valueOf(pageSize), "full_name", "all");
                return PageUtils.toPage(repoList);
//            case "github":
//                List<?> githubRepoList = githubRepoList();
//                return PageUtils.toPage(githubRepoList);
//            case "gitlab":
//                List<?> gitlabRepoList = gitlabRepoList();
//                return PageUtils.toPage(gitlabRepoList);
//            case "gitcode":
        }
        return PageUtils.toPage(repoList);
    }


    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list() {
        List<?> repoList = new ArrayList<>();
        return AjaxResult.success(repoList);
    }

}
