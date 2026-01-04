package vip.gpg123.prometheus.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import vip.gpg123.framework.config.MonitorConfig;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.prometheus.domain.PrometheusGroup;
import vip.gpg123.prometheus.domain.RuleGroup;
import vip.gpg123.prometheus.service.PrometheusGroupService;
import vip.gpg123.prometheus.mapper.PrometheusGroupMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【prometheus_group】的数据库操作Service实现
 * @createDate 2026-01-04 10:05:32
 */
@Service
public class PrometheusGroupServiceImpl extends ServiceImpl<PrometheusGroupMapper, PrometheusGroup> implements PrometheusGroupService {

    @Autowired
    private MonitorConfig.PrometheusProperties prometheusProperties;


    private static final String modelName = "告警规则";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(PrometheusGroup entity) {
        boolean res = super.save(entity);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 文件位置
                String filePath = prometheusProperties.getPath() + "/" + entity.getGroupName() + ".yml";
                List<RuleGroup> ruleGroups = new ArrayList<>();
                ruleGroups.set(0, new RuleGroup(entity.getGroupName(), entity.getInterval(), null));
                // 创建文件
                generateYaml(ruleGroups, filePath);
            }
        });
        return res;
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean res = super.removeById(id);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                if (res) {
                    String filePath = prometheusProperties.getPath() + "/" + id + ".yml";
                    FileUtil.del(filePath);
                }
            }
        });
        return res;
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean updateById(PrometheusGroup entity) {
        boolean res = super.updateById(entity);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                if (res) {
                    String filePath = prometheusProperties.getPath() + "/" + entity.getGroupName() + ".yml";
                    // 修改内容
                    List<RuleGroup> ruleGroups = YamlUtil.loadByPath(filePath, List.class);
                    RuleGroup ruleGroup = ruleGroups.get(0);
                    ruleGroup.setName(entity.getGroupName());
                    ruleGroups.set(0, ruleGroup);
                    generateYaml(ruleGroups, filePath);
                    // 修改文件名称
                    FileUtil.rename(new File(filePath), entity.getGroupName() + ".yml", false);
                }
            }
        });
        return res;
    }

    /**
     * 将 RuleGroup 列表生成 YAML 字符串
     *
     * @param groups 规则组列表
     */
    public static void generateYaml(List<RuleGroup> groups, String filePath) {
        // 设置 YAML 输出格式
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);

        Yaml yaml = new Yaml(options);
        // Prometheus 规则文件的根结构是 { groups: [...] }
        Map<String, Object> root = new HashMap<>();
        root.put("groups", groups);
        try (FileWriter fw = new FileWriter(filePath)) {
            yaml.dump(root, fw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}




