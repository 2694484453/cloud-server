package vip.gpg123.kubernetes.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.setting.yaml.YamlUtil;
import lombok.Data;
import org.springframework.stereotype.Service;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.kubernetes.service.KubernetesConfigService;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

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
        return saveOrUpdateConfig(newConfig);
    }

    /**
     * 更新
     *
     * @param newConfig 配置
     * @return r
     */
    @Override
    public boolean updateConfig(Map<String, Object> newConfig) {
        return saveOrUpdateConfig(newConfig);
    }

    /**
     * 删除配置
     *
     * @param name 名称
     * @return r
     */
    @Override
    public boolean deleteConfig(String name) {
        // 获取原配置
        Map<String,Object> oldConfig = YamlUtil.load(FileUtil.getReader(configFile, StandardCharsets.UTF_8), Map.class);
        // 获取原配置clusters
        JSONArray oldClusters = (JSONArray) oldConfig.get("clusters");
        oldClusters.forEach(oldCluster -> {
            JSONObject oldClusterConfig = (JSONObject) oldCluster;
            if (oldClusterConfig.get("name").equals(name)) {
                oldClusters.remove(oldClusterConfig);
            }
        });

        // 获取原users
        JSONArray oldUsers = (JSONArray) oldConfig.get("users");
        oldUsers.forEach(oldUser -> {
            JSONObject oldUserConfig = (JSONObject) oldUser;
            if (oldUserConfig.get("name").equals(name)) {
                oldUsers.remove(oldUserConfig);
            }
        });

        // 获取原contexts
        JSONArray oldContexts = (JSONArray) oldConfig.get("contexts");
        oldContexts.forEach(oldContext -> {
            JSONObject oldContextConfig = (JSONObject) oldContext;
            if (oldContextConfig.get("name").equals(name)) {
                oldContexts.remove(oldContextConfig);
            }
        });

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

    /**
     * 新增或更新
     * @param newConfig 配置
     * @return r
     */
    private boolean saveOrUpdateConfig(Map<String, Object> newConfig) {
        // 获取原配置
        Map<String, Object> oldConfig = YamlUtil.load(FileUtil.getReader(configFile, StandardCharsets.UTF_8), Map.class);
        // 获取新集群
        JSONArray newClusters = (JSONArray) oldConfig.get("clusters");
        Map<String, Object> newClusterMap = newClusters.stream()
                .map(e -> (JSONObject) e)
                .filter(jsonObj -> jsonObj != null && jsonObj.containsKey("name"))
                .collect(Collectors.toMap(json -> json.get("name").toString(), json -> json));
        // 获取原集群配置
        JSONArray oldClusters = (JSONArray) oldConfig.get("clusters");
        for (int i = 0; i < oldClusters.size(); i++) {
            JSONObject oldCluster = (JSONObject) oldClusters.get(i);
            // 判断是否存在名字
            if (newClusterMap.containsKey(oldCluster.get("name").toString())) {
                // 如果存在就更新
                oldClusters.set(i, newClusterMap.get("name"));
            } else {
                // 直接新增
                oldClusters.add(newClusterMap);
            }
        }
        // 获取新contexts配置
        JSONArray newContexts = (JSONArray) newConfig.get("contexts");
        Map<String, Object> newContextMap = newContexts.stream()
                .map(e -> (JSONObject) e)
                .filter(jsonObj -> jsonObj != null && jsonObj.containsKey("name"))
                .collect(Collectors.toMap(json -> json.get("name").toString(), json -> json));
        // 获取原contexts配置
        JSONArray oldContexts = (JSONArray) oldConfig.get("contexts");
        for (int i = 0; i < oldContexts.size(); i++) {
            JSONObject oldContext = (JSONObject) oldContexts.get(i);
            // 判断是否存在名字
            if (newContextMap.containsKey(oldContext.get("name").toString())) {
                // 如果存在就更新
                newContexts.set(i, newClusterMap.get("name"));
            } else {
                // 直接新增
                newContexts.add(newClusterMap);
            }
        }
        // 获取新users配置
        JSONArray newUsers = (JSONArray) newConfig.get("users");
        Map<String, Object> newUsersMap = newUsers.stream()
                .map(e -> (JSONObject) e)
                .filter(jsonObj -> jsonObj != null && jsonObj.containsKey("name"))
                .collect(Collectors.toMap(json -> json.get("name").toString(), json -> json));
        JSONArray oldUsers = (JSONArray) oldConfig.get("users");
        for (int i = 0; i < oldUsers.size(); i++) {
            JSONObject oldUser = (JSONObject) newUsers.get(i);
            // 判断是否存在名字
            if (newContextMap.containsKey(oldUser.get("name").toString())) {
                // 如果存在就更新
                newUsers.set(i, newUsersMap.get("name"));
            } else {
                // 直接新增
                newUsers.add(newUsersMap);
            }
        }
        // 写入
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            YamlUtil.dump(oldConfig, fileWriter);
        } catch (Exception e) {
            Console.error(e);
            return false;
        }
        return true;
    }
}
