package vip.gpg123.kubernetes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.kubernetes.service.KubernetesClusterService;

@RestController
@RequestMapping("/kubernetes/common")
public class KubernetesCommonController extends BaseController {

    @Autowired
    private KubernetesClusterService kubernetesClusterService;

    @GetMapping("/repoList")
    public AjaxResult repoList() {

        return null;
    }
}
