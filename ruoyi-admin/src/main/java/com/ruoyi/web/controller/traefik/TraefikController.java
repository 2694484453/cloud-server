package com.ruoyi.web.controller.traefik;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/8/31 0:55
 **/
@RestController
@RequestMapping("/traefik")
public class TraefikController {

    @Value("${traefik.path}")
    private String path;

    /**
     * 列表
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        File[] files = FileUtil.ls(path);
        JSONArray jsonArray = JSONUtil.createArray();
        for (File file : files) {
            DateTime lastModified = DateUtil.date(file.lastModified());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", file.getName());
            map.put("path", file.getPath());
            map.put("size", DataSizeUtil.format(file.getTotalSpace()));
            map.put("lastModified", DateUtil.format(lastModified, "yyyy-MM-dd hh:mm:ss"));
            jsonArray.add(map);
        }
        return AjaxResult.success(jsonArray);
    }

    /**
     * 分页
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        JSONArray jsonArray = (JSONArray) ajaxResult.get("data");
        return PageUtils.toPage(jsonArray);
    }
}
