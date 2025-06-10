package vip.gpg123.kubernetes;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.service.KubernetesClusterService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kubernetes")
@Api(value = "集群管理")
public class KubernetesOverViewController extends BaseController {

    @Autowired
    private KubernetesClusterService kubernetesClusterService;

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overView() {
        Map<String, Object> data = new HashMap<>();
        // 总数
        data.put("clusterTotalCount",kubernetesClusterService.count(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUsername())
        ));
        // 正常集群
        data.put("clusterOkCount", kubernetesClusterService.count(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUsername())
                .eq(KubernetesCluster::getStatus, "ok")
        ));
        // 异常集群
        data.put("clusterErrorCount", kubernetesClusterService.count(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUsername())
                .eq(KubernetesCluster::getStatus, "error")
        ));
        return AjaxResult.success(data);
    }
}
