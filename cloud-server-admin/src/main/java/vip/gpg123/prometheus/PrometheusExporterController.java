package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.prometheus.domain.ActiveTarget;
import vip.gpg123.prometheus.domain.PrometheusConfigs;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.prometheus.mapper.PrometheusExporterMapper;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusExporterService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prometheus/exporter")
@Api(tags = "prometheus-exporter管理")
public class PrometheusExporterController extends BaseController {

    @Autowired
    private PrometheusExporterService prometheusExporterService;

    @Autowired
    private PrometheusExporterMapper prometheusExporterMapper;

    @Autowired
    private PrometheusApi prometheusApi;

    @GetMapping("/types")
    public AjaxResult types() {
        List<String> list = new ArrayList<>();
        list.add("caddy-exporter");
        list.add("traefik-exporter");
        list.add("node-exporter");
        list.add("nginx-exporter");
        list.add("spring-boot-exporter");
        list.add("coredns-exporter");
        list.add("amd-smi-exporter");
        list.add("amd-gpu-exporter");
        list.add("jaeger-exporter");
        list.add("kube-state-metrics-exporter");
        return AjaxResult.success(list);
    }

    /**
     * list
     *
     * @param jobName job
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(name = "jobName", required = false) String jobName,
                           @RequestParam(name = "exporterType", required = false) String exporterType) {
        List<PrometheusExporter> list = prometheusExporterService.list(new LambdaQueryWrapper<PrometheusExporter>()
                .like(StrUtil.isNotBlank(jobName), PrometheusExporter::getJobName, jobName)
                .eq(StrUtil.isNotBlank(exporterType), PrometheusExporter::getExporterType, exporterType)
                .orderByDesc(PrometheusExporter::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * page
     *
     * @param jobName j
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(name = "jobName", required = false) String jobName,
                              @RequestParam(name = "exporterType", required = false) String exporterType) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<PrometheusExporter> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        PrometheusExporter exporter = new PrometheusExporter();
        exporter.setJobName(jobName);
        exporter.setExporterType(exporterType);
        exporter.setCreateBy(String.valueOf(getUserId()));
        List<PrometheusExporter> list = prometheusExporterMapper.page(pageDomain, exporter);
        page.setRecords(list);
        page.setTotal(prometheusExporterMapper.list(exporter).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 新增
     *
     * @param exporter ex
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    public AjaxResult add(@RequestBody PrometheusExporter exporter) {
        exporter.setCreateBy(String.valueOf(getUserId()));
        exporter.setCreateTime(DateUtil.date());
        int count = prometheusExporterService.count(new LambdaQueryWrapper<PrometheusExporter>()
                .eq(PrometheusExporter::getJobName, exporter.getJobName())
                .eq(PrometheusExporter::getExporterType, exporter.getExporterType()));
        if (count > 0) {
            return AjaxResult.error("已存在相同类型、名称（已被占用），请更换");
        }
        boolean isSaved = prometheusExporterService.save(exporter);
        return isSaved ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 编辑
     *
     * @param exporter e
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "【编辑】")
    public AjaxResult edit(@RequestBody PrometheusExporter exporter) {
        if (StrUtil.isBlank(exporter.getJobName())) {
            return AjaxResult.error("名称不能为空");
        }
        if (StrUtil.isBlank(exporter.getExporterType())) {
            return AjaxResult.error("exporter类型不能为空");
        }
        PrometheusExporter search = prometheusExporterService.getById(exporter.getId());
        // 修改了名称
        if (!search.getJobName().equals(exporter.getJobName())) {
            int count = prometheusExporterService.count(new LambdaQueryWrapper<PrometheusExporter>()
                    .eq(PrometheusExporter::getJobName, exporter.getJobName())
                    .eq(PrometheusExporter::getExporterType, exporter.getExporterType()));
            if (count > 0) {
                return AjaxResult.error("已存在相同类型、名称（已被占用），请更换");
            }
        }
        exporter.setUpdateBy(String.valueOf(getUserId()));
        exporter.setUpdateTime(DateUtil.date());
        boolean isSuccess = prometheusExporterService.updateById(exporter);
        return isSuccess ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }

    /**
     * 删除
     *
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "【删除】")
    public AjaxResult delete(@RequestParam(value = "id") String id) {
        boolean isSuccess = prometheusExporterService.removeById(id);
        return isSuccess ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * export
     *
     * @param exporter e
     * @param response r
     * @throws IOException e
     */
    @PostMapping("/export")
    @ApiOperation(value = "export")
    public void export(@RequestBody PrometheusExporter exporter, HttpServletResponse response) throws IOException {
        List<PrometheusExporter> list = prometheusExporterService.list(new LambdaQueryWrapper<PrometheusExporter>()
                .eq(StrUtil.isNotBlank(exporter.getExporterType()), PrometheusExporter::getExporterType, exporter.getExporterType())
                .eq(StrUtil.isNotBlank(exporter.getSchemeType()), PrometheusExporter::getSchemeType, exporter.getSchemeType())
                .eq(StrUtil.isNotBlank(exporter.getStatus()), PrometheusExporter::getStatus, exporter.getStatus())
                .eq(PrometheusExporter::getCreateBy, String.valueOf(getUserId()))
                .orderByDesc(PrometheusExporter::getCreateTime)
        );
        JSONArray jsonArray = listToJsonArray(list);
        String jsonStr = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonArray));
        try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            // 1. 设置响应头
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("prometheus-exporter.json", "UTF-8"));
            // 2. 带缓冲的流复制
            IoUtil.copy(IoUtil.toUtf8Stream(jsonStr), out);
        } catch (Exception e) {
            logger.error("文件导出失败：{}", e.getMessage());
            response.sendError(500, "文件导出失败：" + e.getMessage());
        }
    }

    /**
     * 同步状态
     */
    @GetMapping("/syncStatus")
    @ApiOperation(value = "syncStatus")
    public AjaxResult syncStatus() {
        // 查询数据库中
        List<PrometheusExporter> prometheusExporterList = prometheusExporterService.list();
        Map<String, PrometheusExporter> map = prometheusExporterList.stream().collect(Collectors.toMap(PrometheusExporter::getJobName, item -> item));
        // 查询状态prometheus
        PrometheusTargetResponse response = prometheusApi.targets("");
        List<ActiveTarget> list = Convert.toList(ActiveTarget.class, JSONUtil.parseArray(response.getData().getActiveTargets()));
        // 不为空
        if (ObjectUtil.isNotEmpty(prometheusExporterList) && ObjectUtil.isNotEmpty(list)) {
            list.forEach(target -> {
                AtomicBoolean isUpdate = new AtomicBoolean(false);
                String targetName = target.getLabels().getStr("job");
                // 找到了
                if (map.containsKey(targetName)) {
                    String targetStatus = target.getHealth();
                    PrometheusExporter exporter = map.get(targetName);
                    String exportStatus = exporter.getStatus();
                    // 状态发生变化或者是错误原因为空白
                    if (!exportStatus.equals(targetStatus) || (StrUtil.isBlank(exporter.getErrorReason()) && "down".equals(exporter.getStatus()))) {
                        exporter.setStatus(targetStatus);
                        exporter.setGlobalUrl(target.getGlobalUrl());
                        if ("down".equals(targetStatus)) {
                            exporter.setErrorReason(target.getLastError());
                        } else if ("up".equals(targetStatus)) {
                            exporter.setErrorReason(null);
                        } else {
                            exporter.setStatus("unknown");
                            exporter.setErrorReason("unknown");
                        }
                        isUpdate.set(true);
                    }
                    if (isUpdate.get()) {
                        prometheusExporterService.updateById(exporter);
                    }
                }
            });
        }
        return AjaxResult.success();
    }

    /**
     * http动态发现
     *
     * @return r
     */
    @GetMapping("/http-sd")
    @ApiOperation(value = "http-sd")
    public JSONArray httpSd() {
        List<PrometheusExporter> list = prometheusExporterService.list();
        return listToJsonArray(list);
    }

    public JSONArray listToJsonArray(List<PrometheusExporter> list) {
        JSONArray jsonArray = new JSONArray();
        list.forEach(item -> {
            PrometheusConfigs configs = new PrometheusConfigs();
            JSONObject labels = JSONUtil.parseObj(ObjectUtil.isNotNull(item.getLabels()) && JSONUtil.isTypeJSON(item.getLabels().toString()) ? item.getLabels() : new JSONObject());
            labels.set("__scrape_timeout__", ObjectUtil.defaultIfNull(item.getScrapeTimeout() + "s", 15 + "s"));
            labels.set("__scrape_interval__", ObjectUtil.defaultIfNull(item.getScrapeInterval() + "s", 10 + "s"));
            labels.set("__scheme__", ObjectUtil.defaultIfBlank(item.getSchemeType(), "http"));
            labels.set("__metrics_path__", ObjectUtil.defaultIfBlank(item.getMetricsPath(), "/metrics"));
            labels.set("job", item.getJobName());
            labels.set("instance", item.getJobName());
            labels.set("type", ObjectUtil.defaultIfBlank(item.getExporterType(), "unknow"));
            configs.setTargets(Arrays.asList(item.getTargets().split(",")));
            configs.setLabels(labels);
            jsonArray.add(configs);
        });
        return jsonArray;
    }
}
