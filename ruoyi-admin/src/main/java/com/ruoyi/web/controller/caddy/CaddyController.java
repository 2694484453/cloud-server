package com.ruoyi.web.controller.caddy;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:11
 **/
@RestController
@RequestMapping("/caddy")
@Api(tags = "caddy管理")
public class CaddyController {

    @Value("${basePath}")
    private String basePath;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        String path = basePath + "/my-caddy/site";
        File[] files = FileUtil.ls(path);
        List<Map<String, Object>> list = new ArrayList<>();
        for (File file : files) {
            Map<String, Object> map = new HashMap<>();
            map.put("fileName", file.getName());
            map.put("size", DataSizeUtil.format(file.length()));
            map.put("createTime", file.lastModified());
            map.put("filePath", file.getPath());
            list.add(map);
        }
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }
}
