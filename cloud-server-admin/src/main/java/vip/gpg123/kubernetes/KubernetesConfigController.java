package vip.gpg123.kubernetes;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.kubernetes.domain.KubernetesServer;
import vip.gpg123.kubernetes.service.KubernetesConfigService;
import vip.gpg123.kubernetes.service.KubernetesServerService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TimerTask;

@RestController
@RequestMapping("/kubernetes/config")
@Api(tags = "kubernetes配置管理")
public class KubernetesConfigController extends BaseController {

    @Autowired
    private KubernetesServerService kubernetesServerService;

    @Autowired
    private KubernetesConfigService kubernetesConfigService;

    /**
     * 新增
     * @param name 名称
     * @param file 文件
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    public AjaxResult addConfig(@RequestParam(value = "name") String name,
                                @RequestBody MultipartFile file) {
        // 判断是否存在
        long count = kubernetesServerService.count(new LambdaQueryWrapper<KubernetesServer>()
                .eq(KubernetesServer::getContextName, name));
        long countByName = kubernetesServerService.count(new LambdaQueryWrapper<KubernetesServer>()
                .eq(KubernetesServer::getContextName, name)
                .eq(KubernetesServer::getCreateBy, getUsername())
        );
        if (count > 0 && countByName > 0) {
            return AjaxResult.error(name + "配置已经存在，如果要修改请选择更新");
        }
        if (count > 0 && countByName == 0) {
            return AjaxResult.error(name + "名称已被占用，请更换");
        }
        // 获取file
        File tempFile = FileUtil.createTempFile();
        try {
            // 拷贝流
            IoUtil.copy(file.getInputStream(), FileUtil.getOutputStream(tempFile));
            // 解析文件
            Map<String, Object> newConfig = YamlUtil.load(FileUtil.getReader(tempFile, StandardCharsets.UTF_8), Map.class);
            // 校验
            if (!newConfig.containsKey("apiVersion")) {
                return AjaxResult.error("缺少apiVersion");
            }
            if (!newConfig.containsKey("clusters")) {
                return AjaxResult.error("缺少clusters");
            }
            if (!newConfig.containsKey("contexts")) {
                return AjaxResult.error("缺少contexts");
            }
            if (!newConfig.containsKey("users")) {
                return AjaxResult.error("缺少users");
            }
            // 获取clusters
            JSONArray clusters = (JSONArray) newConfig.get("clusters");
            if (clusters.isEmpty()) {
                return AjaxResult.error("clusters不能为空");
            }
            if (clusters.size() > 1) {
                return AjaxResult.error("不能是多个cluster");
            }
            JSONObject cluster = (JSONObject) clusters.get(0);
            cluster.set("name", name);
            clusters.set(0, cluster);
            newConfig.put("clusters", clusters);

            // 获取contexts
            JSONArray contexts = (JSONArray) newConfig.get("contexts");
            if (contexts.isEmpty()) {
                return AjaxResult.error("contexts不能为空");
            }
            if (contexts.size() > 1) {
                return AjaxResult.error("不能是多个context");
            }
            JSONObject context = (JSONObject) contexts.get(0);
            context.set("name", name);
            contexts.set(0, context);
            newConfig.put("contexts", contexts);

            // 获取users
            JSONArray users = (JSONArray) newConfig.get("users");
            if (users.isEmpty()) {
                return AjaxResult.error("users不能为空");
            }
            if (users.size() > 1) {
                return AjaxResult.error("不能是多个user");
            }
            JSONObject user = (JSONObject) users.get(0);
            user.set("name", name);
            users.set(0, user);
            newConfig.put("users", users);
            newConfig.put("current-context", name);

            // 保存
            KubernetesServer kubernetesServer = new KubernetesServer();
            kubernetesServer.setContextName(name);
            kubernetesServer.setClusterOwner(getUsername());
            kubernetesServer.setCreateBy(getUsername());
            kubernetesServer.setConfig(new Yaml().dumpAsMap(newConfig));
            boolean isSuccess = kubernetesServerService.save(kubernetesServer);

            //异步执行
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    // 保存到文件
                    kubernetesConfigService.addConfig(newConfig);
                }
            });
            return isSuccess ? AjaxResult.success() : AjaxResult.error();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
