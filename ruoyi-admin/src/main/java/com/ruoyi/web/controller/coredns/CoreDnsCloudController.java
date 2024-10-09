package com.ruoyi.web.controller.coredns;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:34
 **/
@RestController
@RequestMapping("/coredns")
public class CoreDnsCloudController {

    private static final String path = "config/k3s.gpg123.vip";

    @GetMapping("/list")
    public AjaxResult list() {



        return null;
    }
}
