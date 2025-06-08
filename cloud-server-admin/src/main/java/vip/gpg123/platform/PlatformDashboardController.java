package vip.gpg123.platform;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.domain.domain.DomainRecord;
import vip.gpg123.domain.service.DomainRecordService;
import vip.gpg123.platform.domain.PlatformServiceInstance;
import vip.gpg123.platform.service.PlatformServiceInstanceService;
import vip.gpg123.platform.vo.OverViewVo;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@Api(tags = "面板控制")
public class PlatformDashboardController extends BaseController {

    @Autowired
    private PlatformServiceInstanceService platformServiceInstanceService;

    @Autowired
    private DomainRecordService domainRecordService;
    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overView() {
        OverViewVo overViewVo = new OverViewVo();
        // 实例
        Map<String, Object> service = new LinkedHashMap<>();
        service.put("prometheus", platformServiceInstanceService.count(new LambdaQueryWrapper<PlatformServiceInstance>()
                .eq(PlatformServiceInstance::getCreateBy, getUsername())
                .eq(PlatformServiceInstance::getType, "prometheus")
        ));
        service.put("alertmanager", platformServiceInstanceService.count(new LambdaQueryWrapper<PlatformServiceInstance>()
                .eq(PlatformServiceInstance::getCreateBy, getUsername())
                .eq(PlatformServiceInstance::getType, "alertmanager")
        ));
        service.put("traefik", platformServiceInstanceService.count(new LambdaQueryWrapper<PlatformServiceInstance>()
                .eq(PlatformServiceInstance::getCreateBy, getUsername())
                .eq(PlatformServiceInstance::getType, "traefik")
        ));
        // domain
        Map<String, Object> domain = new LinkedHashMap<>();
        service.put("domain", domain);

        // records
        Map<String, Object> domainRecord = new LinkedHashMap<>();
        domainRecord.put("domainRecord", domainRecordService.count(new LambdaQueryWrapper<DomainRecord>()
                .eq(DomainRecord::getCreateBy, getUsername())
        ));
        // 返回
        overViewVo.setService(service);
        overViewVo.setDomainRecord(domainRecord);
        overViewVo.setDomain(domain);
        return AjaxResult.success(overViewVo);
    }
}
