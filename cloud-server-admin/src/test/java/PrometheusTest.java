import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.common.feign.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusExporterService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class PrometheusTest {

    @Autowired
    private PrometheusExporterService prometheusExporterService;

    @Autowired
    private PrometheusApi  prometheusApi;

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
                                                .eq(StrUtil.isNotBlank(jobName),PrometheusExporter::getJobName, jobName)
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
                                System.out.println(file2.getName()+ "解析失败：" + e.getMessage());
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
}
