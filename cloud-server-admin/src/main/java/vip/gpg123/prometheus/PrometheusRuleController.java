package vip.gpg123.prometheus;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import vip.gpg123.prometheus.domain.PrometheusGroup;
import vip.gpg123.prometheus.domain.PrometheusRule;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import vip.gpg123.prometheus.dto.PrometheusRuleVO;
import vip.gpg123.prometheus.mapper.PrometheusRuleMapper;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusGroupService;
import vip.gpg123.prometheus.service.PrometheusRuleService;
import vip.gpg123.prometheus.service.PrometheusTargetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author gaopuguang
 * @date 2024/11/18 0:44
 **/
@RestController
@RequestMapping("/prometheus/rule")
@Api(tags = "【prometheusRule】管理")
public class PrometheusRuleController extends BaseController {

    @Autowired
    private PrometheusRuleService prometheusRuleService;

    @Autowired
    private PrometheusApi prometheusApi;

    @Autowired
    private PrometheusRuleMapper prometheusRuleMapper;

    /**
     * levels
     */
    @GetMapping("/levels")
    public AjaxResult levels() {
        List<String> list = new ArrayList<>();
        list.add("critical");
        list.add("warning");
        list.add("info");
        return AjaxResult.success(list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(PrometheusRule prometheusRule) {
        List<PrometheusRule> list = prometheusRuleService.list(prometheusRule);
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(Page<PrometheusRule> prometheusRulePage, PrometheusRule prometheusRule) {
        prometheusRule.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        IPage<PrometheusRuleVO> page = prometheusRuleService.pageExtension(prometheusRulePage, prometheusRule);
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 新增
     *
     * @param prometheusRule p
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "add")
    public AjaxResult add(@RequestBody PrometheusRule prometheusRule) {
        prometheusRule.setCreateBy(SecurityUtils.getUserId().toString());
        prometheusRule.setCreateTime(DateUtil.date());
        boolean result = prometheusRuleService.save(prometheusRule);
        return result ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }

    /**
     * 修改
     *
     * @param prometheusRule pr
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "edit")
    public AjaxResult edit(@RequestBody PrometheusRule prometheusRule) {
        prometheusRule.setUpdateBy(SecurityUtils.getUserId().toString());
        prometheusRule.setUpdateTime(DateUtil.date());
        boolean result = prometheusRuleService.updateById(prometheusRule);
        return result ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }

    /**
     * delete
     *
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "delete")
    public AjaxResult delete(@RequestParam(value = "id") String id) {
        boolean result = prometheusRuleService.removeById(id);
        return result ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 更新状态
     */
    @GetMapping("/syncStatus")
    @ApiOperation(value = "syncStatus")
    public void syncStatus() {
        List<PrometheusRule> prometheusRules = prometheusRuleMapper.list(new PrometheusRule());
        Map<String,PrometheusRule> map = prometheusRules.stream().collect(Collectors.toMap(prometheusRule -> prometheusRule.getRuleId().toString(), Function.identity()));
        JSONObject jsonObject = prometheusApi.rules("alert");
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray groups = data.getJSONArray("groups");
        // 遍历
        groups.forEach(group -> {
            JSONObject groupJsonObject = JSONUtil.parseObj(group);
            JSONArray rules = groupJsonObject.getJSONArray("rules");
            rules.forEach(rule -> {
                boolean isUpdate = false;
                JSONObject ruleJsonObject = JSONUtil.parseObj(rule);
                // 规则健康
                String prometheusRuleStatus = ruleJsonObject.getStr("health");
                // 规则状态
                String prometheusRuleState = ruleJsonObject.getStr("state");
                // labels
                JSONObject labels = ruleJsonObject.getJSONObject("labels");
                // 是否存在
                if (labels.containsKey("id") && map.containsKey(labels.getStr("id"))) {
                    PrometheusRule prometheusRule = map.get(labels.getStr("id"));
                    // 状态
                    String status = ObjectUtil.defaultIfBlank(prometheusRule.getStatus(), "");
                    String alertStatus = ObjectUtil.defaultIfBlank(prometheusRule.getStatus(), "");
                    // 是否更新状态
                    if (!status.equals(prometheusRuleStatus)) {
                        prometheusRule.setStatus(prometheusRuleStatus);
                        isUpdate = true;
                    }
                    if (!alertStatus.equals(prometheusRuleState)) {
                        prometheusRule.setRuleState(prometheusRuleState);
                        isUpdate = true;
                    }
                    if (isUpdate) {
                        System.out.println("更新：" + prometheusRule.getRuleName());
                        prometheusRuleMapper.updateById(prometheusRule);
                    }
                }
            });
        });
    }
}
