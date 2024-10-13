package com.ruoyi.web.controller.traefik;

import com.ruoyi.common.K8sUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.api.model.ListOptionsBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/10/12 23:49
 **/
@RestController
@RequestMapping("/traefikCloud")
public class TraefikCloudController {

    @GetMapping("/list")
    public AjaxResult list() {
        ListOptions listOptions = new ListOptionsBuilder()
                .withKind("IngressRoute")
                .withApiVersion("v1alpha1")
                .build();
        CustomResourceDefinitionList list = K8sUtil.createKClient().apiextensions().v1().customResourceDefinitions().list(listOptions);
        list.getItems().forEach(System.out::println);
        return null;
    }
}
