package vip.gpg123.git;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.git.domain.GitToken;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.mapper.GitRepoMapper;
import vip.gpg123.git.service.GitTokenService;
import vip.gpg123.git.service.GitRepoService;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/16 0:08
 **/
@RestController
@RequestMapping("/git/repo")
@Api(tags = "【git】仓库管理")
public class GitRepoController extends BaseController {

    @Autowired
    private GitTokenService gitTokenService;

    @Autowired
    private GitRepoService gitRepoService;

    @Autowired
    private GitRepoMapper gitRepoMapper;

    /**
     * 列表查询
     *
     * @param type 类型
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "name", required = false) String name) {
        return AjaxResult.success(gitRepoService.list(new LambdaQueryWrapper<GitRepo>()
                .eq(StrUtil.isNotBlank(type), GitRepo::getType, type)
                .like(StrUtil.isNotBlank(name), GitRepo::getName, name)
                .eq(GitRepo::getCreateBy, getUserId())
        ));
    }

    /**
     * 分页查询
     *
     * @param type 类型
     * @param name 名称
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(value = "type", required = false) String type,
                              @RequestParam(value = "name", required = false) String name) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<GitRepo> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        GitRepo gitRepo = new GitRepo();
        gitRepo.setType(type);
        gitRepo.setName(name);
        gitRepo.setCreateBy(String.valueOf(getUserId()));

        List<GitRepo> list = gitRepoMapper.page(pageDomain, gitRepo);
        page.setRecords(list);
        page.setTotal(gitRepoMapper.list(gitRepo).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 新增
     *
     * @param gitRepo gitRepo
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    public AjaxResult addRepo(@RequestBody GitRepo gitRepo) {
        // 设置信息
        gitRepo.setCreateBy(getUsername());
        gitRepo.setCreateTime(DateUtil.date());
        boolean isSaved = gitRepoService.saveOrUpdate(gitRepo);
        return isSaved ? success("新增成功") : error("新增失败");
    }

    /**
     * 导入
     *
     * @param gitRepo gitRepo
     * @return r
     */
    @PostMapping("/import")
    @ApiOperation(value = "【导入】")
    public AjaxResult importRepo(@RequestBody GitRepo gitRepo) {
        // 设置信息
        gitRepo.setCreateBy(getUsername());
        gitRepo.setCreateTime(DateUtil.date());
        GitRepo search = gitRepoService.getOne(new LambdaQueryWrapper<GitRepo>()
                .eq(GitRepo::getId, gitRepo.getId())
                .eq(GitRepo::getCreateBy, getUsername())
        );
        boolean isSaved = gitRepoService.saveOrUpdate(gitRepo);
        if (search != null) {
            return isSaved ? success("更新成功") : error("更新失败");
        } else {
            return isSaved ? success("导入成功") : error("导入失败");
        }
    }

    /**
     * 详情
     *
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "【详情】")
    public AjaxResult info(@RequestParam(value = "id") String id) {
        GitRepo gitRepo = gitRepoService.getById(id);
        return AjaxResult.success(gitRepo);
    }


    /**
     * 删除
     *
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "【删除】")
    public AjaxResult delete(@RequestParam(value = "id") String id) {
        boolean remove = gitRepoService.removeById(id);
        return remove ? success() : error();
    }


    /**
     * 新增或更新
     *
     * @param gitRepos gitRepos
     * @param type     类型
     * @return r
     */
    @PostMapping("/importBatch")
    @ApiOperation(value = "【新增或更新】")
    public AjaxResult insertOrUpdate(@RequestBody List<GitRepo> gitRepos, @RequestParam(value = "type") String type) {
        // 获取token
        GitToken gitToken = gitTokenService.getOne(new LambdaQueryWrapper<GitToken>()
                .eq(GitToken::getType, type)
                .eq(GitToken::getCreateBy, getUsername())
        );
        if (gitToken == null) {
            throw new RuntimeException("请先添加" + type + "类型认证");
        }
        // 开始执行新增或更新
        gitRepos.forEach(e -> {
            e.setCreateBy(getUsername());
            e.setCreateTime(DateUtil.date());
        });
        if (!gitRepos.isEmpty()) {
            boolean isSaved = gitRepoService.saveOrUpdateBatch(gitRepos);
            if (!isSaved) {
                throw new RuntimeException("保存失败");
            }
        }
        return AjaxResult.success("保存成功");
    }
}
