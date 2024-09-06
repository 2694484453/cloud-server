package com.ruoyi.web.controller.cluster;

import cn.hutool.setting.Setting;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaopuguang
 * @date 2024/9/6 22:39
 **/
@RestController
@RequestMapping("/cluster")
@Api(value = "集群管理")
public class ClusterController {


    /**
     * 集群列表
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    private AjaxResult list() {

        return null;
    }

    public static Setting getSetting(){
        return new Setting("config/config");
    }
}
