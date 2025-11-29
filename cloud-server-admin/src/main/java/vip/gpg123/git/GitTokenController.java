package vip.gpg123.git;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.domain.GitToken;
import vip.gpg123.git.mapper.GitTokenMapper;
import vip.gpg123.git.service.GitTokenService;
import vip.gpg123.git.service.GiteeApiService;
import vip.gpg123.git.service.GithubApiService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/git/accessRepo")
@Api(tags = "【git】认证查询仓库管理")
public class GitTokenController extends BaseController {

    @Autowired
    private GitTokenService gitTokenService;

    @Autowired
    private GitTokenMapper gitTokenMapper;

    @Autowired
    private GiteeApiService giteeApiService;

    @Autowired
    private GithubApiService githubApiService;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(value = "type") String type) {
        // 获取token
        GitToken gitToken = gitTokenService.getOne(new LambdaQueryWrapper<GitToken>()
                .eq(GitToken::getType, type)
                .eq(GitToken::getCreateBy, getUsername())
        );
        if (gitToken == null) {
            throw new RuntimeException("请先添加" + type + "类型认证");
        }
        //
        String accessToken = gitToken.getAccessToken();
        Integer pageNum = 1;
        List<?> repoList = new ArrayList<>();
        switch (type) {
            case "gitee":
                repoList = giteeApiService.repos(accessToken, String.valueOf(pageNum), String.valueOf(100), "full_name", "all");
                return AjaxResult.success(repoList);
            case "github":
                repoList = githubApiService.repos("Bearer " + accessToken, String.valueOf(pageNum), String.valueOf(9999), "created", "all");
                return AjaxResult.success(repoList);
        }
        return AjaxResult.success(repoList);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo repos(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "type") String type) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<GitToken> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        // 获取token
        GitToken gitToken = new GitToken();
        gitToken.setType(type);
        gitToken.setName(name);
        gitToken.setCreateBy(String.valueOf(getUserId()));

        // 查询
        List<GitToken> list = gitTokenMapper.page(pageDomain, gitToken);
        page.setRecords(list);
        page.setTotal(gitTokenMapper.list(gitToken).size());
        return PageUtils.toPage(list);
    }

}
