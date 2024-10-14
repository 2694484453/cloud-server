package com.ruoyi.web.controller.traefik;

import cn.hutool.core.convert.Convert;
import com.ruoyi.common.K8sUtil;
import com.ruoyi.common.core.domain.AjaxResult;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.api.model.networking.v1.IngressList;
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
        IngressList list = K8sUtil.createKClient().network().v1().ingresses().list();
        List<Ingress> ingressList = list.getItems();
        return AjaxResult.success(ingressList);
    }

    @GetMapping("/page")
    public TableDataInfo page() {
        List ingressList = Convert.convert(List.class, list().get("data"));
        return PageUtils.toPage(ingressList);
    }
}
