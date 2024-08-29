package com.ruoyi.web.controller.monitor;

import cn.hutool.core.lang.Console;
import com.ruoyi.common.base.ExecResponse;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.NerdCtlUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaopuguang
 * @date 2024/8/30 1:50
 **/
@RestController
@RequestMapping("/imageRepo")
public class ImageRepoController {


    @GetMapping("/list")
    public AjaxResult list() {
        ExecResponse execResponse = NerdCtlUtil.execFor("nerdctl", "images", "--format", "json");
        String data = execResponse.getSuccessMsg();
        Console.log("{}", data);
        return execResponse.getExistCode() == 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
