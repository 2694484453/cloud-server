package vip.gpg123.admin.repo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.yaml.YamlUtil;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/8/30 23:50
 **/
@RestController
@RequestMapping("/helmRepo")
public class HelmRepoController {

    private final static String url = getSetting().getStr("index", "helm", "https://helm-repo.gpg123.vip/index.yaml");

    private final static String repoName = getSetting().getStr("repoName", "helm", "gpg_dev");

    /**
     * 查询list
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        HttpResponse httpResponse = HttpUtil.createGet(url).
                timeout(3000).
                contentType(ContentType.JSON.getValue()).
                charset(StandardCharsets.UTF_8).execute();
        String res = httpResponse.body();
        Map map = YamlUtil.load(IoUtil.toUtf8Stream(res), Map.class);
        JSONArray jsonArray = JSONUtil.createArray();
        Map<String, Object> enties = Convert.toMap(String.class, Object.class, map.get("entries"));
        enties.forEach((k, v) -> {
            List<Object> list = Convert.toList(Object.class, v);
            if (!list.isEmpty()) {
                jsonArray.addAll(list);
            }
        });
        return AjaxResult.success(jsonArray);
    }

    /**
     * 分页查询
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

    @GetMapping("info")
    @ApiOperation(value = "查看详情")
    public AjaxResult info(@RequestParam(value = "name") String name,
                           @RequestParam(value = "version", required = false) String version,
                           @RequestParam(value = "type", required = false, defaultValue = "values") String type) {
        String[] init = {"helm", "show"};
        switch (type) {
            case "values":
                init = ArrayUtil.append(init, "values", repoName + "/" + name);
                break;
            case "readme":
                init = ArrayUtil.append(init, "readme", repoName + "/" + name);
        }
        String res = RuntimeUtil.execForStr(init);
        return null;
    }

    public static Setting getSetting() {
        return new Setting("config/config");
    }
}
