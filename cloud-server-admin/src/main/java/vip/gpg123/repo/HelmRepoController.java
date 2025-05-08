package vip.gpg123.repo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.helm.domain.ChartApp;
import vip.gpg123.common.utils.helm.HelmApp;
import vip.gpg123.common.utils.helm.HelmUtils;
import vip.gpg123.repo.domain.HelmRepo;
import vip.gpg123.repo.domain.HelmRepoResponse;
import vip.gpg123.repo.service.HelmRepoApiService;

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

    @Autowired
    private HelmRepoApiService helmRepoApiService;

    /**
     * 查询list
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        // 请求仓库列表
        String res =  helmRepoApiService.index();
        HelmRepoResponse repoResponse = YamlUtil.load(StrUtil.getReader(res),HelmRepoResponse.class);
        Map<String, List<HelmRepo>> entries = repoResponse.getEntries();
        String generated = repoResponse.getGenerated();
        // 返回结果
        List<ChartApp> chartApps = new ArrayList<>();
        List<HelmApp> helmApps = HelmUtils.list("", SecurityUtils.getUsername());
        Map<String,HelmApp> helmAppMap = new HashMap<>();
        CollectionUtil.toMap(helmApps, helmAppMap,HelmApp::getName);
        entries.forEach((k, v) -> {
            ChartApp chartApp = new ChartApp();
            // 一个key就是一个应用，list数量即为版本数量
            chartApp.setName(k);
            chartApp.setVersionCount(v.size());
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
