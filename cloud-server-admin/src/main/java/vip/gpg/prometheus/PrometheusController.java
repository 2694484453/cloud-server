package vip.gpg.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/8/21 22:50
 **/
@RequestMapping("/monitor")
@RestController
@Api(value = "监控中心")
public class PrometheusController {

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
        type = StrUtil.isBlank(type) ? "spring-boot" : type;
        File file = chooseTheFile(type);
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        JSONArray res = JSONUtil.createArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> objectMap = Convert.toMap(String.class, Object.class, jsonArray.get(i));
            objectMap.put("id", i);
            res.add(objectMap);
        }
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
     * 删除
     *
     * @param index i
     * @param type  t
     * @return r
     */
    @DeleteMapping("delete")
    @ApiOperation("删除监控")
    public AjaxResult delete(@RequestParam(value = "index") String index,
                             @RequestParam(value = "type") String type) {
        // 读取
        File file = chooseTheFile(type);
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        jsonArray.remove(index);
        // 格式化
        String fmt = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonArray));
        // 写入
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fmt);
            fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success();
    }

    /**
     * 查询详情
     * @param id i
     * @param type t
     * @return r
     */
    @GetMapping("/detail")
    @ApiOperation(value = "查询详情")
    public AjaxResult detail(@RequestParam(value = "id") Integer id,
                             @RequestParam(value = "type") String type) {
        // 读取
        File file = chooseTheFile(type);
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        return AjaxResult.success(jsonArray.get(id));
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
