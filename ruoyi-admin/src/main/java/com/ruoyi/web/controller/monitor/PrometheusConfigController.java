package com.ruoyi.web.controller.monitor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/8/21 22:50
 **/
@RequestMapping("/monitor")
@RestController
@Api(value = "监控中心")
public class PrometheusConfigController {

    @Value("${prometheus.spring-boot}")
    private String springBoot;

    @Value("${prometheus.node-exporter}")
    private String nodeExporter;

    @Value("${prometheus.nginx-exporter}")
    private String nginxExporter;

    @Value("${prometheus.caddy}")
    private String caddy;

    @Value("${prometheus.traefik}")
    private String traefik;


    /**
     * 支持类型查询
     *
     * @return r
     */
    @GetMapping("/typeList")
    @ApiOperation("类型查询")
    public AjaxResult typeList() {
        String[] types = {};
        types = ArrayUtil.append(types, "node-exporter",
                "spring-boot",
                "nginx-exporter",
                "caddy",
                "traefik");
        return AjaxResult.success(types);
    }

    /**
     * 监控list
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation("监控分页查询")
    public AjaxResult page(@RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "name", required = false) String name) {
        type = StrUtil.isBlank(type) ? springBoot : type;
        File file = FileUtil.file(type);
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        return AjaxResult.success(jsonArray);
    }

    /**
     * 新增监控
     *
     * @param map m
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation("新增监控")
    public AjaxResult add(@RequestBody Map<String, Object> map) {
        String name = map.get("name").toString();
        String targets = map.get("targets").toString();
        String type = map.get("type").toString();
        Object labels = map.get("labels");
        // 读取
        File file = chooseTheFile(type);
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        jsonArray.add(map);
        // 写到文件里
        String fmt = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonArray));
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fmt);
            fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return AjaxResult.success(true);
    }

    /**
     * 选择文件
     *
     * @param type t
     * @return r
     */
    private File chooseTheFile(String type) {
        File file = null;
        switch (type) {
            case "spring-boot":
                file = FileUtil.file(springBoot);
                break;
            case "node-exporter":
                file = FileUtil.file(nodeExporter);
                break;
            case "nginx-exporter":
                file = FileUtil.file(nginxExporter);
                break;
            case "caddy":
                file = FileUtil.file(caddy);
                break;
            case "traefik":
                file = FileUtil.file(traefik);
                break;
        }
        return file;
    }
}
