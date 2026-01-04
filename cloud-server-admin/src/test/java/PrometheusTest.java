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
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.prometheus.PrometheusTargetController;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import vip.gpg123.prometheus.domain.PrometheusRule;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusTargetService;
import vip.gpg123.prometheus.service.PrometheusRuleService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class PrometheusTest {

    @Autowired
    private PrometheusTargetService prometheusTargetService;

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
                                        PrometheusTarget exporter = new PrometheusTarget();
                                        //
                                        JSONObject labels = ((JSONObject) item).getJSONObject("labels");
                                        String jobName = labels.get("job").toString();
                                        //
                                        JSONArray targets = ((JSONObject) item).getJSONArray("targets");
                                        String targetsStr = StrUtil.join(",", targets);
                                        //
                                        PrometheusTarget search = prometheusTargetService.getOne(new LambdaQueryWrapper<PrometheusTarget>()
                                                .eq(StrUtil.isNotBlank(jobName), PrometheusTarget::getJobName, jobName)
                                        );
                                        if (search != null) {
                                            // 更新
                                            exporter = search;
                                            exporter.setUpdateBy("ark.gpg123.vip");
                                            exporter.setTargets(targetsStr);
                                            exporter.setUpdateTime(DateUtil.date());
                                            prometheusTargetService.updateById(exporter);
                                        } else {
                                            exporter.setJobName(jobName);
                                            exporter.setTargets(targetsStr);
                                            exporter.setExporterType(type);
                                            exporter.setCreateBy("ark.gpg123.vip");
                                            exporter.setCreateTime(DateUtil.date());
                                            prometheusTargetService.save(exporter);
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
    public void reloadRules() {
        prometheusApi.reload();
    }

    @Test
    public void syncStatus() {
        SpringUtils.getBean(PrometheusTargetController.class).syncStatus();
    }
}
