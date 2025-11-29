package vip.gpg123.git;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import vip.gpg123.git.domain.GitCodeSpace;
import vip.gpg123.git.mapper.GitCodeSpaceMapper;
import vip.gpg123.git.service.GitCodeSpaceService;

import java.util.List;

@RestController
@RequestMapping("/git/codeSpace")
public class GitCodeSpaceController extends BaseController {

    @Autowired
    private GitCodeSpaceMapper gitCodeSpaceMapper;

    @Autowired
    private GitCodeSpaceService gitCodeSpaceService;

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
     * 详情
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
     * 查询是否存在codeSpace
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
            return AjaxResult.success("不存在", false);
        } else {
            return AjaxResult.success("已存在", true);
        }
    }

}
