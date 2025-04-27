package vip.gpg123.repo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.helm.domain.ChartApp;
import vip.gpg123.common.utils.helm.HelmApp;
import vip.gpg123.common.utils.helm.HelmUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/8/30 23:50
 **/
@RestController
@RequestMapping("/helmRepo")
public class HelmRepoController {

    @Value("${repo.helm.url}")
    private String url;

    @Value("${repo.helm.name}")
    private String repoName;

    /**
     * 查询list
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        // 请求仓库列表
        HttpResponse httpResponse = HttpUtil.createGet(url+"/index.yaml").
                timeout(10000).
                setConnectionTimeout(10000).
                contentType(ContentType.JSON.getValue()).
                charset(StandardCharsets.UTF_8).execute();
        String res = httpResponse.body();
        Map map = YamlUtil.load(IoUtil.toUtf8Stream(res), Map.class);
        Map<String, Object> enties = Convert.toMap(String.class, Object.class, map.get("entries"));
        String generated = map.get("generated").toString();
        // 返回结果
        List<ChartApp> chartApps = new ArrayList<>();
        List<HelmApp> helmApps = HelmUtils.list("", SecurityUtils.getUsername());
        Map<String,HelmApp> helmAppMap = new HashMap<>();
        CollectionUtil.toMap(helmApps, helmAppMap,HelmApp::getName);
        enties.forEach((k, v) -> {
            ChartApp chartApp = new ChartApp();
            // 一个key就是一个应用，list数量即为版本数量
            chartApp.setName(k);
            chartApp.setVersionCount(JSONUtil.parseArray(v).size());
            chartApp.setItems(v);
            chartApp.setGenerated(generated);
            chartApp.setType("application");
            chartApp.setRepoName(repoName);
            chartApp.setRepoUrl(url);
            // 检查是否已经安装
            chartApp.setIsInstalled(helmAppMap.containsKey(k));
            chartApps.add(chartApp);
        });
        return AjaxResult.success(chartApps);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> list = Convert.toList(list().get("data"));
        return PageUtils.toPage(list);
    }


    /**
     * 查询详情
     *
     * @param name    名称
     * @param version 版本
     * @param type    类型
     * @return r
     */
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
}
