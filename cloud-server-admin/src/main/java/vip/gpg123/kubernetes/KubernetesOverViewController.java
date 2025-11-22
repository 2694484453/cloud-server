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


}
