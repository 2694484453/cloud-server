package vip.gpg123.kubernetes.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONArray;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;
import org.springframework.stereotype.Service;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.kubernetes.service.KubernetesConfigService;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Data
@Service
public class KubernetesConfigServiceImpl implements KubernetesConfigService {

    private static final String configFilePath = K8sUtil.defaultConfigFilePath();

    private File configFile = new File(configFilePath);

    /**
     * 添加
     *
     * @param newConfig 配置
     * @return r
     */
    @Override
    public boolean addConfig(Map<String, Object> newConfig) {
        // 获取原配置
        Map<String,Object> oldConfig = YamlUtil.load(FileUtil.getReader(configFile, StandardCharsets.UTF_8), Map.class);
        // 获取原配置clusters
        JSONArray oldClusters = (JSONArray) oldConfig.get("clusters");
        JSONArray newClusters = (JSONArray) newConfig.get("clusters");
        oldClusters.addAll(newClusters);

        // 获取原users
        JSONArray oldUsers = (JSONArray) oldConfig.get("users");
        JSONArray newUsers = (JSONArray) newConfig.get("users");
        oldUsers.addAll(newUsers);

        // 获取原contexts
        JSONArray oldContexts = (JSONArray) oldConfig.get("contexts");
        JSONArray newContexts = (JSONArray) newConfig.get("contexts");
        oldContexts.addAll(newContexts);

        // 重新设置
        oldConfig.put("contexts", oldContexts);
        oldConfig.put("users", oldUsers);
        oldConfig.put("clusters", oldClusters);

        // 写入
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            YamlUtil.dump(oldConfig,fileWriter);
        } catch (Exception e) {
            Console.error(e);
            return false;
        }
        return false;
    }

}
