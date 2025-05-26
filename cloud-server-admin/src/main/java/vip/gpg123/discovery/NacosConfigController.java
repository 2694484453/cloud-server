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
import vip.gpg123.discovery.domain.NacosConfig;
import vip.gpg123.discovery.service.NacosApiService;
import vip.gpg123.discovery.service.NacosConfigService;

import java.util.List;

@RestController
@RequestMapping("/nacos/config")
@Api(tags = "【nacos】配置管理")
public class NacosConfigController extends BaseController {

    @Autowired
    private NacosConfigService nacosConfigService;

    @Autowired
    private NacosApiService nacosApiService;

    /**
     * 列表查询
     *
     * @param name 名称
     * @param type 类型
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "type", required = false) String type) {
        List<?> list = nacosConfigService.list(new LambdaQueryWrapper<NacosConfig>()
                .eq(NacosConfig::getCreateBy, getUsername())
                .eq(StrUtil.isNotBlank(type), NacosConfig::getConfigFileType, type)
                .like(StrUtil.isNotBlank(name), NacosConfig::getConfigName, name)
                .orderByDesc(NacosConfig::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param name 配置名称
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "type", required = false) String type) {
        IPage<NacosConfig> page = nacosConfigService.page(new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize()),
                new LambdaQueryWrapper<NacosConfig>()
                        .eq(NacosConfig::getCreateBy, getUsername())
                        .eq(StrUtil.isNotBlank(type), NacosConfig::getConfigFileType, type)
                        .like(StrUtil.isNotBlank(name), NacosConfig::getConfigName, name)
                        .orderByDesc(NacosConfig::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }
}
