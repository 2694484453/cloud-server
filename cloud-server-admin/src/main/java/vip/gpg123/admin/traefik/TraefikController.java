package vip.gpg123.admin.traefik;

import cn.hutool.core.convert.Convert;
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
import java.util.LinkedHashMap;
import java.util.List;
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
            map.put("length", DataSizeUtil.format(file.length()));
            map.put("config", FileUtil.readString(file, StandardCharsets.UTF_8));
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
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }

    /**
     * 获取信息
     *
     * @param fileName 文件
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取信息")
    public AjaxResult info(@RequestParam("fileName") String fileName) {
        String filePath = path + "/" + fileName;
        List<String> lines = FileUtil.readLines(filePath, StandardCharsets.UTF_8);
        return AjaxResult.success(lines);
    }

    /**
     * 新增
     *
     * @param params 参数
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody Map<String, String> params) {
        String filePath = path + "/" + params.get("fileName");
        String content = params.get("content");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return FileUtil.exist(filePath) ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 新增
     *
     * @param params 参数
     * @return r
     */
    @PostMapping("/edit")
    @ApiOperation(value = "新增")
    public AjaxResult edit(@RequestBody Map<String, String> params) {
        String filePath = path + "/" + params.get("fileName");
        String content = params.get("content");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return FileUtil.exist(filePath) ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 执行删除
     *
     * @param fileName 文件名
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam("fileName") String fileName) {
        String filePath = path + "/" + fileName;
        try {
            FileUtil.del(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return FileUtil.exist(filePath) ? AjaxResult.error("删除失败") : AjaxResult.success("删除成功");
    }
}
