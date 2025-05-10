package vip.gpg123.docker;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.docker.domain.DockerRepo;
import vip.gpg123.docker.service.DockerRepoService;

import java.util.List;

@RestController
@RequestMapping("/docker/repo")
@Api(tags = "docker仓库管理")
public class DockerRepoController extends BaseController {

    @Autowired
    private DockerRepoService dockerRepoService;

    /**
     * 列表查询
     * @param name 名称
     * @param type 类型
     * @return r
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "type", required = false) String type) {
        List<DockerRepo> list = dockerRepoService.list(new LambdaQueryWrapper<DockerRepo>()
                .like(StrUtil.isNotBlank(name), DockerRepo::getRepoName, name)
                .eq(StrUtil.isNotBlank(type), DockerRepo::getRepoType, type)
                .eq(DockerRepo::getCreateBy, getUsername())
                .orderByDesc(DockerRepo::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param name 名称
     * @param type 类型
     * @return r
     */
    @GetMapping(value = "/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "type", required = false) String type) {
        IPage<DockerRepo> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = dockerRepoService.page(page,new LambdaQueryWrapper<DockerRepo>()
                .like(StrUtil.isNotBlank(name), DockerRepo::getRepoName, name)
                .eq(StrUtil.isNotBlank(type), DockerRepo::getRepoType, type)
                .eq(DockerRepo::getCreateBy, getUsername())
                .orderByDesc(DockerRepo::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }

}
