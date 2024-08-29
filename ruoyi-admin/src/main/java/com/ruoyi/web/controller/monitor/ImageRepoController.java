package com.ruoyi.web.controller.monitor;

import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.base.ExecResponse;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.NerdCtlUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/8/30 1:50
 **/
@RestController
@RequestMapping("/imageRepo")
public class ImageRepoController {


    /**
     * 支持类型查询
     *
     * @return r
     */
    @GetMapping("/typeList")
    @ApiOperation("类型查询")
    public AjaxResult typeList() {
        String[] types = {};
        types = ArrayUtil.append(types, "linux/amd64",
                "linux/386",
                "linux/arm64/v8");
        return AjaxResult.success(types);
    }

    /**
     * list
     * @param namespace
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "namespace", required = false) String namespace) {
        String[] init = {};

        ExecResponse execResponse = NerdCtlUtil.execFor("nerdctl", "images", "--format", "json");
        String data = execResponse.getSuccessMsg();
        Console.log("{}", data);
        JSONArray jsonArray = JSONUtil.createArray();
        try (BufferedReader br = new BufferedReader(new StringReader(data))) {
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                Map map = JSONUtil.toBean(line, Map.class);
                jsonArray.add(map);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return execResponse.getExistCode() == 0 ? AjaxResult.success(jsonArray) : AjaxResult.error();
    }
}
