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
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.git.domain.GitAccess;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.service.GitAccessService;
import vip.gpg123.git.service.GitRepoService;

import java.util.ArrayList;
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
    private GitAccessService gitAccessService;

    @Autowired
    private GitRepoService gitRepoService;

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
                .eq(GitRepo::getCreateBy, getUsername())
        ));
    }

    /**
     * 分页查询
     * @param type 类型
     * @param name 名称
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(value = "type", required = false) String type,
                              @RequestParam(value = "name", required = false) String name) {
        IPage<GitRepo> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = gitRepoService.page(page, new LambdaQueryWrapper<GitRepo>()
                .eq(StrUtil.isNotBlank(type), GitRepo::getType, type)
                .like(StrUtil.isNotBlank(name), GitRepo::getName, name)
                .eq(GitRepo::getCreateBy, getUsername())
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 新增
     * @param gitRepo gitRepo
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    public AjaxResult add(@RequestBody GitRepo gitRepo) {
        gitRepo.setCreateBy(getUsername());
        gitRepo.setCreateTime(DateUtil.date());
        boolean isSaved = gitRepoService.save(gitRepo);
        return isSaved ? success() : error();
    }

    /**
     * 详情
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
    @PostMapping("/import")
    @ApiOperation(value = "【新增或更新】")
    public AjaxResult insertOrUpdate(@RequestBody List<GitRepo> gitRepos, @RequestParam(value = "type") String type) {
        // 获取token
        GitAccess gitAccess = gitAccessService.getOne(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getType, type)
                .eq(GitAccess::getCreateBy, getUsername())
        );
        if (gitAccess == null) {
            throw new RuntimeException("请先添加" + type + "类型认证");
        }
        // 开始执行新增或更新
        gitRepos.forEach(gitRepo -> {
            gitRepo.setCreateBy(getUsername());
            gitRepo.setCreateTime(DateUtil.date());
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
