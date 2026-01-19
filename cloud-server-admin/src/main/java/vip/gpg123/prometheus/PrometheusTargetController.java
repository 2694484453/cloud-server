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
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.prometheus.domain.ActiveTarget;
import vip.gpg123.prometheus.domain.PrometheusConfigs;
import vip.gpg123.prometheus.domain.PrometheusRule;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.prometheus.mapper.PrometheusTargetMapper;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusRuleService;
import vip.gpg123.prometheus.service.PrometheusTargetService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prometheus/exporter")
@Api(tags = "prometheus-exporter管理")
public class PrometheusTargetController extends BaseController {

    @Autowired
    private PrometheusTargetService prometheusTargetService;

    @Autowired
    private PrometheusTargetMapper prometheusTargetMapper;

    @Autowired
    private PrometheusRuleService prometheusRuleService;

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
     * @param prometheusTarget t
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(PrometheusTarget prometheusTarget) {
        prometheusTarget.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        List<PrometheusTarget> list = prometheusTargetService.list(prometheusTarget);
        return AjaxResult.success(list);
    }

    /**
     * page
     *
     * @param prometheusTarget t
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(Page<PrometheusTarget> prometheusTargetPage, PrometheusTarget prometheusTarget) {
        prometheusTarget.setCreateBy(String.valueOf(getUserId()));
        IPage<PrometheusTarget> page = prometheusTargetMapper.page(prometheusTargetPage, prometheusTarget);
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
    public AjaxResult add(@RequestBody PrometheusTarget exporter) {
        exporter.setCreateBy(String.valueOf(getUserId()));
        exporter.setCreateTime(DateUtil.date());
        int count = prometheusTargetService.count(new LambdaQueryWrapper<PrometheusTarget>()
                .eq(PrometheusTarget::getJobName, exporter.getJobName())
                .eq(PrometheusTarget::getExporterType, exporter.getExporterType()));
        if (count > 0) {
            return AjaxResult.error("已存在相同类型、名称（已被占用），请更换");
        }
        boolean isSaved = prometheusTargetService.save(exporter);
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
    public AjaxResult edit(@RequestBody PrometheusTarget exporter) {
        if (StrUtil.isBlank(exporter.getJobName())) {
            return AjaxResult.error("名称不能为空");
        }
        if (StrUtil.isBlank(exporter.getExporterType())) {
            return AjaxResult.error("exporter类型不能为空");
        }
        PrometheusTarget search = prometheusTargetService.getById(exporter.getTargetId());
        // 修改了名称
        if (!search.getJobName().equals(exporter.getJobName())) {
            int count = prometheusTargetService.count(new LambdaQueryWrapper<PrometheusTarget>()
                    .eq(PrometheusTarget::getJobName, exporter.getJobName())
                    .eq(PrometheusTarget::getExporterType, exporter.getExporterType()));
            if (count > 0) {
                return AjaxResult.error("已存在相同类型、名称（已被占用），请更换");
            }
        }
        exporter.setUpdateBy(String.valueOf(getUserId()));
        exporter.setUpdateTime(DateUtil.date());
        boolean isSuccess = prometheusTargetService.updateById(exporter);
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
        // 查询是否被使用
        long count = prometheusRuleService.count(new LambdaQueryWrapper<PrometheusRule>()
                .eq(PrometheusRule::getGroupId,id)
        );
        if (count > 0) {
            return AjaxResult.error("请先删除该端点下的告警规则");
        }
        boolean isSuccess = prometheusTargetService.removeById(id);
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
    public void export(@RequestBody PrometheusTarget exporter, HttpServletResponse response) throws IOException {
        List<PrometheusTarget> list = prometheusTargetService.list(new LambdaQueryWrapper<PrometheusTarget>()
                .eq(StrUtil.isNotBlank(exporter.getExporterType()), PrometheusTarget::getExporterType, exporter.getExporterType())
                .eq(StrUtil.isNotBlank(exporter.getSchemeType()), PrometheusTarget::getSchemeType, exporter.getSchemeType())
                .eq(StrUtil.isNotBlank(exporter.getStatus()), PrometheusTarget::getStatus, exporter.getStatus())
                .eq(PrometheusTarget::getCreateBy, String.valueOf(getUserId()))
                .orderByDesc(PrometheusTarget::getCreateTime)
        );
        JSONArray jsonArray = listToJsonArray(list);
        String jsonStr = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonArray));
        try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            // 1. 设置响应头
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("prometheus-exporter.json", StandardCharsets.UTF_8));
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
        List<PrometheusTarget> prometheusTargetList = prometheusTargetService.list();
        Map<String, PrometheusTarget> map = prometheusTargetList.stream().collect(Collectors.toMap(target -> String.valueOf(target.getTargetId()), Function.identity()));
        // 查询状态prometheus
        PrometheusTargetResponse response = prometheusApi.targets("");
        List<ActiveTarget> list = Convert.toList(ActiveTarget.class, JSONUtil.parseArray(response.getData().getActiveTargets()));
        // 不为空
        if (ObjectUtil.isNotEmpty(prometheusTargetList) && ObjectUtil.isNotEmpty(list)) {
            list.forEach(target -> {
                AtomicBoolean isUpdate = new AtomicBoolean(false);
                // labels
                JSONObject labels = target.getLabels();
                // 判断是否包含id且相等
                if (labels.containsKey("id") && map.containsKey(labels.getStr("id"))) {
                    // id
                    String targetId = labels.getStr("id");
                    String targetStatus = target.getHealth();
                    PrometheusTarget exporter = map.get(targetId);
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
                        prometheusTargetService.updateById(exporter);
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
        List<PrometheusTarget> list = prometheusTargetService.list();
        return listToJsonArray(list);
    }

    public JSONArray listToJsonArray(List<PrometheusTarget> list) {
        JSONArray jsonArray = new JSONArray();
        list.forEach(item -> {
            PrometheusConfigs configs = new PrometheusConfigs();
            JSONObject labels = JSONUtil.parseObj(ObjectUtil.isNotNull(item.getLabels()) && JSONUtil.isTypeJSON(item.getLabels().toString()) ? item.getLabels() : new JSONObject());
            labels.set("__scrape_timeout__", ObjectUtil.defaultIfNull(item.getScrapeTimeout() + "s", 15 + "s"));
            labels.set("__scrape_interval__", ObjectUtil.defaultIfNull(item.getScrapeInterval() + "s", 10 + "s"));
            labels.set("__scheme__", ObjectUtil.defaultIfBlank(item.getSchemeType(), "http"));
            labels.set("__metrics_path__", ObjectUtil.defaultIfBlank(item.getMetricsPath(), "/metrics"));
            labels.set("job", item.getJobName());
            labels.set("id", item.getTargetId().toString());
            labels.set("instance", item.getJobName());
            labels.set("type", ObjectUtil.defaultIfBlank(item.getExporterType(), "unknow"));
            labels.set("description", item.getDescription());
            configs.setTargets(Arrays.asList(item.getTargets().split(",")));
            configs.setLabels(labels);
            jsonArray.add(configs);
        });
        return jsonArray;
    }
}
