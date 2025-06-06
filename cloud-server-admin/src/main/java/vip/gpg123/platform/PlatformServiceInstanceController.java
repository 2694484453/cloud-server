package vip.gpg123.platform;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.platform.domain.PlatformServiceInstance;
import vip.gpg123.platform.service.PlatformServiceInstanceService;

import java.util.List;

@RestController
@RequestMapping("/platform/service")
@Api(tags = "平台服务实例管理")
public class PlatformServiceInstanceController extends BaseController {

    @Autowired
    private PlatformServiceInstanceService platformServiceInstanceService;

    /**
     * 分页查询
     *
     * @param platformServiceInstance 参数
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(PlatformServiceInstance platformServiceInstance) {
        IPage<PlatformServiceInstance> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = platformServiceInstanceService.page(page, new LambdaQueryWrapper<>(platformServiceInstance)
                .eq(StrUtil.isNotBlank(platformServiceInstance.getId()), PlatformServiceInstance::getId, platformServiceInstance.getId())
                .eq(StrUtil.isNotBlank(platformServiceInstance.getName()), PlatformServiceInstance::getName, platformServiceInstance.getName())
                .like(StrUtil.isNotBlank(platformServiceInstance.getDescription()), PlatformServiceInstance::getDescription, platformServiceInstance.getDescription())
                .eq(StrUtil.isNotBlank(platformServiceInstance.getStatus()), PlatformServiceInstance::getStatus, platformServiceInstance.getStatus())
                .eq(StrUtil.isNotBlank(platformServiceInstance.getType()), PlatformServiceInstance::getType, platformServiceInstance.getType())
                .eq(PlatformServiceInstance::getCreateBy, platformServiceInstance.getCreateBy())
                .orderByDesc(PlatformServiceInstance::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 列表查询
     * @param platformServiceInstance 参数
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(PlatformServiceInstance platformServiceInstance) {
        List<PlatformServiceInstance> list = platformServiceInstanceService.list(new LambdaQueryWrapper<>(platformServiceInstance)
                .eq(StrUtil.isNotBlank(platformServiceInstance.getId()), PlatformServiceInstance::getId, platformServiceInstance.getId())
                .eq(StrUtil.isNotBlank(platformServiceInstance.getName()), PlatformServiceInstance::getName, platformServiceInstance.getName())
                .like(StrUtil.isNotBlank(platformServiceInstance.getDescription()), PlatformServiceInstance::getDescription, platformServiceInstance.getDescription())
                .eq(StrUtil.isNotBlank(platformServiceInstance.getStatus()), PlatformServiceInstance::getStatus, platformServiceInstance.getStatus())
                .eq(StrUtil.isNotBlank(platformServiceInstance.getType()), PlatformServiceInstance::getType, platformServiceInstance.getType())
                .eq(PlatformServiceInstance::getCreateBy, platformServiceInstance.getCreateBy())
                .orderByDesc(PlatformServiceInstance::getCreateTime)
        );
        return AjaxResult.success(list);
    }
}
