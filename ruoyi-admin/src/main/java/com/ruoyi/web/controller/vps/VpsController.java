package com.ruoyi.web.controller.vps;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.web.controller.vps.domain.Servers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/6 1:54
 **/
@RestController
@RequestMapping("/vps")
@Api(tags = "vps主机")
public class VpsController {

    private static final String serverFilePath = "/root/.aliyun/servers/servers.json";

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        File file = new File(serverFilePath);
        JSONArray jsonArray = JSONUtil.readJSONArray(file,StandardCharsets.UTF_8);
        List<Servers> serversList = JSONUtil.toList(jsonArray, Servers.class);
        return AjaxResult.success(serversList);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<Servers> serversList = Convert.toList(Servers.class, list().get("data"));
        return PageUtils.toPage(serversList);
    }
}
