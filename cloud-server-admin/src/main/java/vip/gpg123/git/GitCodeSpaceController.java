package vip.gpg123.git;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.git.domain.GitCodeSpace;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.mapper.GitCodeSpaceMapper;
import vip.gpg123.git.service.GitCodeSpaceService;
import vip.gpg123.git.service.GitRepoService;

import java.util.List;

@RestController
@RequestMapping("/git/codeSpace")
public class GitCodeSpaceController extends BaseController {

    @Autowired
    private GitRepoService gitRepoService;

    @Autowired
    private GitCodeSpaceMapper gitCodeSpaceMapper;

    @Autowired
    private GitCodeSpaceService gitCodeSpaceService;

    @Value("${ide.path}")
    private String basePath;

    @Value("${ide.url}")
    private String url;

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
        return AjaxResult.success(gitCodeSpaceService.list(new LambdaQueryWrapper<GitCodeSpace>()
                .eq(StrUtil.isNotBlank(type), GitCodeSpace::getType, type)
                .like(StrUtil.isNotBlank(name), GitCodeSpace::getSpaceName, name)
                .eq(GitCodeSpace::getCreateBy, getUserId())
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
        IPage<GitCodeSpace> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        GitCodeSpace gitCodeSpace = new GitCodeSpace();
        gitCodeSpace.setType(type);
        gitCodeSpace.setSpaceName(name);
        gitCodeSpace.setCreateBy(String.valueOf(getUserId()));

        List<GitCodeSpace> list = gitCodeSpaceMapper.page(pageDomain, gitCodeSpace);
        page.setRecords(list);
        page.setTotal(gitCodeSpaceMapper.list(gitCodeSpace).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 新增
     *
     * @param gitCodeSpace git
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    public AjaxResult add(@RequestBody GitCodeSpace gitCodeSpace) {
        //
        GitRepo gitRepo = gitRepoService.getById(gitCodeSpace.getRepoId());
        if (ObjectUtil.isNull(gitRepo)) {
            return AjaxResult.error("repoId错误");
        }
        gitCodeSpace.setRepoUrl(gitRepo.getUrl());
        gitCodeSpace.setCreateBy(String.valueOf(getUserId()));
        gitCodeSpace.setCreateTime(DateUtil.date());
        String spacePath = basePath + "/" + getUserId() + "/" + gitCodeSpace.getSpaceName();
        gitCodeSpace.setSpaceUrl(url + "?folder=" + spacePath);
        gitCodeSpace.setSpacePath(spacePath);
        boolean save = gitCodeSpaceService.save(gitCodeSpace);
        return save ? AjaxResult.success("操作成功", gitCodeSpace) : AjaxResult.error("操作失败", false);
    }

    /**
     * 详情
     *
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "【详情】")
    public AjaxResult info(@RequestParam(value = "id") Integer id) {
        GitCodeSpace gitCodeSpace = gitCodeSpaceMapper.selectById(id);
        return AjaxResult.success(gitCodeSpace);
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "【删除】")
    public AjaxResult delete(@RequestParam(value = "id") Integer id) {
        boolean result = gitCodeSpaceService.removeById(id);
        return result ? AjaxResult.success("删除成功", id) : AjaxResult.error("删除失败", false);
    }

    /**
     * 查询是否存在codeSpace
     *
     * @param repoId id
     * @return r
     */
    @GetMapping("/isExist")
    @ApiOperation(value = "【查询是否存在codeSpace】")
    public AjaxResult isExist(@RequestParam(value = "repoId") String repoId) {
        List<GitCodeSpace> list = gitCodeSpaceMapper.selectList(new LambdaQueryWrapper<GitCodeSpace>()
                .eq(GitCodeSpace::getRepoId, repoId)
        );
        if (list.isEmpty()) {
            return AjaxResult.success("不存在！", null);
        } else if (list.size() == 1) {
            GitCodeSpace gitCodeSpace = list.get(0);
            return AjaxResult.success("仓库关联的gitCodeSpace已存在，正在跳转中...", gitCodeSpace);
        } else {
            return AjaxResult.error("未知错误");
        }
    }
}
