import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.app.HelmAppMarketController;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.domain.PrometheusRule;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusExporterService;
import vip.gpg123.prometheus.service.PrometheusRuleService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class PrometheusTest {

    @Autowired
    private PrometheusExporterService prometheusExporterService;

    @Autowired
    private PrometheusRuleService prometheusRuleService;

    @Autowired
    private PrometheusApi prometheusApi;

    @Autowired
    private HelmAppMarketController  helmAppMarketController;

    @Test
    public void insert() {
        String path = "/Volumes/gaopuguang/project/prometheus/exporter";

        File[] files1 = FileUtil.ls(path);
//        for (File file : files) {
//            System.out.println(file.getName());
//        }
        for (File file : files1) {
            if (FileUtil.isDirectory(file)) {
                String type = file.getName();
                // 再循环
                List<File> files = FileUtil.loopFiles(file.getPath());
                if (!files.isEmpty()) {
                    for (File file2 : files) {
                        if (file2.getName().endsWith(".json")) {

                            // 解析endpoint
                            try {
                                System.out.println(file2 + "开始解析：");
                                JSONArray jsonArray = JSONUtil.readJSONArray(file2, StandardCharsets.UTF_8);
                                if (!jsonArray.isEmpty()) {
                                    jsonArray.forEach(item -> {
                                        PrometheusExporter exporter = new PrometheusExporter();
                                        //
                                        JSONObject labels = ((JSONObject) item).getJSONObject("labels");
                                        String jobName = labels.get("job").toString();
                                        //
                                        JSONArray targets = ((JSONObject) item).getJSONArray("targets");
                                        String targetsStr = StrUtil.join(",", targets);
                                        //
                                        PrometheusExporter search = prometheusExporterService.getOne(new LambdaQueryWrapper<PrometheusExporter>()
                                                .eq(StrUtil.isNotBlank(jobName), PrometheusExporter::getJobName, jobName)
                                        );
                                        if (search != null) {
                                            // 更新
                                            exporter = search;
                                            exporter.setUpdateBy("ark.gpg123.vip");
                                            exporter.setTargets(targetsStr);
                                            exporter.setUpdateTime(DateUtil.date());
                                            prometheusExporterService.updateById(exporter);
                                        } else {
                                            exporter.setJobName(jobName);
                                            exporter.setTargets(targetsStr);
                                            exporter.setExporterType(type);
                                            exporter.setCreateBy("ark.gpg123.vip");
                                            exporter.setCreateTime(DateUtil.date());
                                            prometheusExporterService.save(exporter);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println(file2.getName() + "解析失败：" + e.getMessage());
                            }
                        }
                    }
                }

            }
        }
    }

    @Test
    public void export() {
        PrometheusTargetResponse response = prometheusApi.targets("");
        System.out.println(response.getData());
    }

    @Test
    public void createRules() {
        String path = "/Volumes/gaopuguang/project/prometheus/rules";
        List<File> files = FileUtil.loopFiles(FileUtil.file(path));
        for (File file : files) {
            System.out.println(file.getName());
            if (file.getName().endsWith(".yml") && !file.getName().startsWith(".")) {
                System.out.println(file.getName());
                // 获取parentName
                String parentDirName = file.getParentFile().getName();
                // 解析yaml
                Map<String, Object> map = YamlUtil.loadByPath(file.getPath(), Map.class);
                JSONObject jsonObject = JSONUtil.parseObj(map);
                JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("groups"));
                jsonArray.forEach(item -> {
                    JSONObject object = (JSONObject) item;
                    String groupName = object.get("name").toString();
                    JSONArray rules = object.getJSONArray("rules");
                    rules.forEach(e -> {
                        PrometheusRule prometheusRule = new PrometheusRule();
                        JSONObject rule = JSONUtil.parseObj(e);
                        String alert = rule.get("alert").toString();
                        String expr = rule.get("expr").toString();
                        JSONObject labels = rule.containsKey("labels") ? (JSONObject) rule.get("labels") : new JSONObject();
                        JSONObject annotations = rule.containsKey("annotations") ? (JSONObject) rule.get("annotations") : new JSONObject();
                        String forTime = rule.get("for").toString();
                        //
                        prometheusRule.setAlertName(alert);
                        prometheusRule.setGroupName(groupName);
                        prometheusRule.setExpr(expr);
                        prometheusRule.setType(parentDirName);
                        prometheusRule.setCreateBy("1");
                        prometheusRule.setLabels(labels.toString());
                        prometheusRule.setAnnotations(annotations.toString());
                        prometheusRule.setForTime(forTime);
                        prometheusRule.setCreateTime(DateUtil.date());

                        prometheusRuleService.save(prometheusRule);
                    });
                });
            }
        }
    }

}
