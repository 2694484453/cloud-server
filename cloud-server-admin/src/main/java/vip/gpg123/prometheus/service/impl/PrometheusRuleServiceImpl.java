package vip.gpg123.prometheus.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.config.MonitorConfig;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.producer.MessageProducer;
import vip.gpg123.prometheus.domain.PrometheusRule;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import vip.gpg123.prometheus.dto.RuleFileProps;
import vip.gpg123.prometheus.dto.RuleGroup;
import vip.gpg123.prometheus.dto.PrometheusRuleVO;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusRuleService;
import vip.gpg123.prometheus.mapper.PrometheusRuleMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.prometheus.service.PrometheusTargetService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【prometheus_rule】的数据库操作Service实现
 * @createDate 2025-12-14 19:28:20
 */
@Service
public class PrometheusRuleServiceImpl extends ServiceImpl<PrometheusRuleMapper, PrometheusRule> implements PrometheusRuleService {

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private MonitorConfig.PrometheusProperties prometheusProperties;

    @Autowired
    private PrometheusRuleMapper prometheusRuleMapper;

    @Autowired
    private PrometheusTargetService prometheusTargetService;

    @Autowired
    private PrometheusApi prometheusApi;

    private static final String modelName = "告警规则";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(PrometheusRule entity) {
        boolean res = super.save(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                PrometheusTarget prometheusTarget = prometheusTargetService.getById(entity.getGroupId());
                // 文件位置
                String filePath = prometheusProperties.getPath() + "/" + prometheusTarget.getJobName() + ".yml";
                createFile(filePath, prometheusTarget.getJobName(), sysUser, entity);
                // 发送
                messageProducer.sendEmail("新增", modelName, res, sysUser, true);
                // 刷新
                prometheusApi.reload();
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
        PrometheusRule prometheusRule = baseMapper.selectById(id);
        boolean res = super.removeById(id);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                PrometheusTarget prometheusTarget = prometheusTargetService.getById(prometheusRule.getGroupId());
                // 文件位置
                String filePath = prometheusProperties.getPath() + "/" + prometheusTarget.getJobName() + ".yml";
                if (FileUtil.exist(filePath)) {
                    // 读取
                    List<RuleGroup> ruleGroups = readYaml(filePath);
                    RuleGroup ruleGroup = ruleGroups.get(0);
                    List<RuleFileProps> rules = ruleGroup.getRules();
                    // 删除规则
                    rules.removeIf(item -> {
                        Map<String, Object> labels = item.getLabels();
                        if (labels != null && labels.containsKey("id")) {
                            labels.get("id");
                        }
                        return false;
                    });
                    ruleGroup.setRules(rules);
                    ruleGroups.set(0, ruleGroup);
                    generateYaml(ruleGroups, filePath);
                }
                // 发送
                messageProducer.sendEmail("删除", modelName, res, sysUser, true);
                // 刷新
                prometheusApi.reload();
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
    public boolean updateById(PrometheusRule entity) {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        boolean res = super.updateById(entity);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                PrometheusTarget prometheusTarget = prometheusTargetService.getById(entity.getGroupId());
                String filePath = prometheusProperties.getPath() + "/" + prometheusTarget.getJobName() + ".yml";
                // 如果文件存在
                if (FileUtil.exist(filePath)) {
                    List<RuleGroup> ruleGroups = readYaml(filePath);
                    RuleGroup ruleGroup = ruleGroups.get(0);
                    List<RuleFileProps> rules = ruleGroup.getRules();
                    rules.forEach(item -> {
                        Map<String, Object> labels = item.getLabels();
                        if (labels != null && labels.containsKey("id") && labels.get("id").equals(String.valueOf(entity.getRuleId()))) {
                            //
                            labels.put("id", String.valueOf(entity.getRuleId()));
                            labels.put("createBy", sysUser.getUserId());
                            labels.put("severity", entity.getSeverityLevel());
                            item.setLabels(labels);
                            //
                            Map<String, Object> annotations = new HashMap<>();
                            annotations.put("summary", String.format("\"%s\"", entity.getSummary()));
                            annotations.put("description", entity.getDescription());
                            item.setAnnotations(annotations);
                            item.setExpr(entity.getExpr() + "m");
                            item.setAlert(entity.getRuleName());
                        }
                    });
                    ruleGroup.setRules(rules);
                    ruleGroups.set(0, ruleGroup);
                    generateYaml(ruleGroups, filePath);
                } else {
                    createFile(filePath, prometheusTarget.getJobName(), sysUser, entity);
                }
                // 发送
                messageProducer.sendEmail("修改", modelName, res, sysUser, true);
                // 刷新
                prometheusApi.reload();
            }
        });
        return res;
    }

    public static List<RuleGroup> readYaml(String filePath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //
        try {
            Map data = mapper.readValue(new File(filePath), Map.class);
            Object groups = data.get("groups");
            return (List<RuleGroup>) groups;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 RuleGroup 列表生成 YAML 字符串
     *
     * @param groups 规则组列表
     */
    public static void generateYaml(List<RuleGroup> groups, String filePath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //
        Map<String, Object> root = new HashMap<>();
        root.put("groups", groups);
        try (FileWriter fw = new FileWriter(filePath)) {
            // 写入
            mapper.writeValue(fw, root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * page
     *
     * @param page           page
     * @param prometheusRule r
     * @return r
     */
    @Override
    public IPage<PrometheusRule> page(Page<PrometheusRule> page, PrometheusRule prometheusRule) {
        return prometheusRuleMapper.page(page, prometheusRule);
    }

    /**
     * page
     *
     * @param page           page
     * @param prometheusRule r
     * @return r
     */
    @Override
    public IPage<PrometheusRuleVO> pageExtension(Page<PrometheusRule> page, PrometheusRule prometheusRule) {
        return prometheusRuleMapper.pageExtension(page, prometheusRule);
    }

    /**
     * list
     *
     * @param prometheusRule r
     * @return r
     */
    @Override
    public List<PrometheusRule> list(PrometheusRule prometheusRule) {
        return prometheusRuleMapper.list(prometheusRule);
    }

    public void createFile(String filePath, String groupName, SysUser sysUser, PrometheusRule entity) {
        List<RuleGroup> ruleGroups = new ArrayList<>();
        List<RuleFileProps> rules = new ArrayList<>();
        RuleGroup ruleGroup = new RuleGroup();
        // 添加
        RuleFileProps ruleFileProps = new RuleFileProps();
        ruleFileProps.setAlert(entity.getRuleName());
        ruleFileProps.setExpr(entity.getExpr() + "m");
        //
        Map<String, Object> annotations = new HashMap<>();
        annotations.put("summary", String.format("\"%s\"", entity.getSummary()));
        annotations.put("description", entity.getDescription());
        ruleFileProps.setAnnotations(annotations);
        //
        Map<String, Object> labels = new HashMap<>();
        labels.put("createBy", String.valueOf(sysUser.getUserId()));
        labels.put("severity", entity.getSeverityLevel());
        labels.put("id", String.valueOf(entity.getRuleId()));
        labels.put("groupId", String.valueOf(entity.getGroupId()));
        ruleFileProps.setLabels(labels);
        rules.add(ruleFileProps);
        ruleGroup.setRules(rules);
        ruleGroup.setName(groupName);
        ruleGroups.add(ruleGroup);
        generateYaml(ruleGroups, filePath);
    }
}




