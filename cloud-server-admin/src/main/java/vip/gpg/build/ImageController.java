package vip.gpg.build;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
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
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/9/1 16:06
 **/
@RestController
@RequestMapping("/build/image")
public class ImageController {

    @Value("${build.image}")
    private String image;

    /**
     * 支持类型查询
     *
     * @return r
     */
    @GetMapping("/typeList")
    @ApiOperation(value = "类型查询")
    public AjaxResult typeList() {
        String[] types = {};
        types = ArrayUtil.append(types, "gitee",
                "github",
                "gitlab");
        /*types = ArrayUtil.append(types, "linux/amd64",
                "linux/arm64",
                "linux/arm/v7",
                "windows/amd64",
                "windows/arm/v6");*/
        return AjaxResult.success(types);
    }

    /**
     * 仓库地址
     * @return r
     */
    @GetMapping("/repoList")
    @ApiOperation(value = "仓库地址查询")
    public AjaxResult repoList() {
        String[] types = {};
        types = ArrayUtil.append(types,
                "registry.cn-hangzhou.aliyuncs.com",
                "repo.gpg123.vip");

        return AjaxResult.success(types);
    }

    /**
     * 列表查询
     *
     * @param name n
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name) {
        File file = FileUtil.file(image + "/" + "index.json");
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        return AjaxResult.success(jsonArray);
    }

    /**
     * 分页查询
     *
     * @param name n
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name) {
        AjaxResult ajaxResult = list(name);
        JSONArray jsonArray = (JSONArray) ajaxResult.get("data");
        return PageUtils.toPage(jsonArray);
    }

    /**
     * 新增
     *
     * @param params p
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody Map<String, Object> params) {
        String name = params.get("name").toString();
        String type = params.get("type").toString();
        String namespace = params.get("namespace").toString();
        String version = params.get("version").toString();
        String repo = params.get("repo").toString();
        String timeOut = params.get("timeOut").toString();
        String remark = params.get("remark").toString();
        // set other
        params.put("createTime", DateUtil.date().toString());
        params.put("updateTime", "");
        params.put("status", "0");
        params.remove("file");
        //
        String fileDir = image + "/" + "index.json";
        File file = FileUtil.file(fileDir);
        JSONArray jsonArray = JSONUtil.readJSONArray(file, StandardCharsets.UTF_8);
        jsonArray.add(params);
        // 写入
        try {
            FileWriter fileWriter = new FileWriter(file);
            String formatStr = JSONUtil.formatJsonStr(jsonArray.toString());
            fileWriter.write(formatStr);
            fileWriter.close();
            // 创建文件夹
            FileUtil.mkdir(image + "/" + name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(true);
    }
}
