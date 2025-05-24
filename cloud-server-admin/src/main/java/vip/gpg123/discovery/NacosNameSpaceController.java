package vip.gpg123.discovery;

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
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.discovery.domain.NacosNameSpace;
import vip.gpg123.discovery.service.NacosNameSpaceService;
import vip.gpg123.framework.config.domain.NacosClient;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/3/2 15:39
 **/
@RestController
@RequestMapping("/discovery/nacos/namespace")
@Api(tags = "【discovery】nacos命名空间管理")
public class NacosNameSpaceController extends BaseController {

    @Autowired
    private NacosClient nacosClient;

    @Autowired
    private NacosNameSpaceService nacosNameSpaceService;

    /**
     * 列表查询
     *
     * @param nameSpace 名称
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "nameSpace", required = false) String nameSpace) {
        List<?> list = nacosNameSpaceService.list(new LambdaQueryWrapper<NacosNameSpace>()
                .eq(NacosNameSpace::getCreateBy, getUsername())
                .like(StrUtil.isNotBlank(nameSpace), NacosNameSpace::getNameSpace, nameSpace)
                .orderByDesc(NacosNameSpace::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param nameSpace 名称
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "nameSpace", required = false) String nameSpace) {
        IPage<NacosNameSpace> page = new Page<>(TableSupport.buildPageRequest().getPageNum(),TableSupport.buildPageRequest().getPageSize());
        page = nacosNameSpaceService.page(page, new LambdaQueryWrapper<NacosNameSpace>()
                .eq(NacosNameSpace::getCreateBy, getUsername())
                .like(StrUtil.isNotBlank(nameSpace), NacosNameSpace::getNameSpace, nameSpace)
                .orderByDesc(NacosNameSpace::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }
}
