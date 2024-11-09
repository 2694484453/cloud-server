package com.ruoyi.web.controller.repo;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
    @ApiOperation(value = "类型查询")
    public AjaxResult typeList() {
        String[] types = {};
        types = ArrayUtil.append(types, "linux/amd64",
                "linux/386",
                "linux/arm64/v8");
        return AjaxResult.success(types);
    }

    /**
     * 命名空间
     *
     * @return r
     */
    @GetMapping("/namespaceList")
    @ApiOperation(value = "命名空间列表")
    public AjaxResult namespaceList() {
        String[] init = {};
        init = ArrayUtil.append(init, "nerdctl", "namespace", "list", "--format", "json");
        String response = RuntimeUtil.execForStr(init);
        Console.log("{}", response);
        JSONArray jsonArray = JSONUtil.createArray();
        try (BufferedReader br = new BufferedReader(new StringReader(response))) {
            String line;
            while ((line = br.readLine()) != null) {
                Map map = JSONUtil.toBean(line, Map.class);
                jsonArray.add(map);
            }
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(jsonArray);
    }

    /**
     * 镜像list
     *
     * @param namespace ns
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "镜像列表")
    public AjaxResult list(@RequestParam(value = "namespace", required = false) String namespace,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "type", required = false) String type) {
        String[] init = {};
        init = ArrayUtil.append(init, "docker", "images", "--format", "json");
        // 名称检索
        if (StrUtil.isNotBlank(name)) {
            init = ArrayUtil.append(init, "--filter", "Repository" + name);
        }
        // 类型检索
        if (StrUtil.isNotBlank(type)) {
            init = ArrayUtil.append(init, "--filter", "Platform=" + type);
        }
        // ns检索
        if (StrUtil.isNotBlank(namespace)) {
            init = ArrayUtil.append(init, "--namespace", namespace);
        }
        String response = RuntimeUtil.execForStr(init);
        Console.log("{}", response);
        JSONArray jsonArray = JSONUtil.createArray();
        try (BufferedReader br = new BufferedReader(new StringReader(response))) {
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                Map map = JSONUtil.toBean(line, Map.class);
                jsonArray.add(map);
            }
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success(jsonArray);
    }

    /**
     * page查询
     *
     * @param namespace ns
     * @param name      n
     * @param type      t
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "namespace", required = false) String namespace,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "type", required = false) String type) {
        AjaxResult ajaxResult = list(namespace, name, type);
        JSONArray jsonArray = (JSONArray) ajaxResult.get("data");
        return PageUtils.toPage(jsonArray);
    }

}
