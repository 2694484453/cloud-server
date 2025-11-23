package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.platform.domain.ActiveTarget;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusExporterService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prometheus/exporter")
public class PrometheusExporterController {

    @Value("${monitor.prometheus.exporterPath}")
    private String path;

    @Autowired
    private PrometheusExporterService prometheusExporterService;

    @Autowired
    private PrometheusApi prometheusApi;

    /**
     * list
     * @param jobName job
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(name = "jobName", required = false) String jobName,
                           @RequestParam(name = "exporterType",required = false) String exporterType) {
        List<PrometheusExporter> list = prometheusExporterService.list(new LambdaQueryWrapper<PrometheusExporter>()
                .like(StrUtil.isNotBlank(jobName), PrometheusExporter::getJobName, jobName)
                .eq(StrUtil.isNotBlank(exporterType), PrometheusExporter::getExporterType, exporterType)
                .orderByDesc(PrometheusExporter::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * page
     * @param page p
     * @param jobName j
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(Page<PrometheusExporter> page,
                              @RequestParam(name = "jobName", required = false) String jobName,
                              @RequestParam(name = "exporterType",required = false) String exporterType) {
        // 获取分页参数
        PageUtils.toIPage(page);
        IPage<PrometheusExporter> pageRes = prometheusExporterService.page(page, new LambdaQueryWrapper<PrometheusExporter>()
                .like(StrUtil.isNotBlank(jobName), PrometheusExporter::getJobName, jobName)
                .eq(StrUtil.isNotBlank(exporterType), PrometheusExporter::getExporterType, exporterType)
                .orderByDesc(PrometheusExporter::getCreateTime)
        );
        return PageUtils.toPageByIPage(pageRes);
    }

    /**
     * 同步数据
     * @return r
     */
    @GetMapping("/syncFile")
    public AjaxResult sync() {
        File[] files1 = FileUtil.ls(path);
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
                                            exporter.setUpdateBy(SecurityUtils.getUsername());
                                            exporter.setTargets(targetsStr);
                                            exporter.setUpdateTime(DateUtil.date());
                                            prometheusExporterService.updateById(exporter);
                                        } else {
                                            exporter.setJobName(jobName);
                                            exporter.setTargets(targetsStr);
                                            exporter.setExporterType(type);
                                            exporter.setCreateBy(SecurityUtils.getUsername());
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
        return null;
    }

    /**
     * 同步状态
     */
    @GetMapping("/syncStatus")
    public AjaxResult syncStatus() {
        // 查询数据库中
        List<PrometheusExporter> prometheusExporterList = prometheusExporterService.list();
        Map<String, PrometheusExporter> map = prometheusExporterList.stream().collect(Collectors.toMap(PrometheusExporter::getJobName, item -> item));
        // 查询状态prometheus
        PrometheusTargetResponse response = prometheusApi.targets("");
        List<ActiveTarget> list = Convert.toList(ActiveTarget.class, JSONUtil.parseArray(response.getData().getActiveTargets()));
        // 循环
        list.forEach(target -> {
            JSONObject object = JSONUtil.parseObj(target.getDiscoveredLabels());
            String jobName = object.getStr("job");
            String status = target.getHealth();
            if (map.containsKey(jobName)) {
                PrometheusExporter exporter = map.get(jobName);
                exporter.setStatus(status);
                exporter.setGlobalUrl(target.getGlobalUrl());
                prometheusExporterService.updateById(exporter);
            }
        });
        return AjaxResult.success();
    }
}
