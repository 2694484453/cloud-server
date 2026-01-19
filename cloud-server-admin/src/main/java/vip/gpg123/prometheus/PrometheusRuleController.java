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
import vip.gpg123.prometheus.mapper.PrometheusRuleMapper;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusGroupService;
import vip.gpg123.prometheus.service.PrometheusRuleService;
import vip.gpg123.prometheus.service.PrometheusTargetService;

import java.util.ArrayList;
import java.util.List;

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
    private PrometheusRuleMapper prometheusRuleMapper;

    @Autowired
    private PrometheusGroupService prometheusGroupService;

    @Autowired
    private PrometheusApi prometheusApi;
    private PrometheusTargetService prometheusTargetService;

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
        IPage<PrometheusRule> page = prometheusRuleService.page(prometheusRulePage, prometheusRule);
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
        // 查询
        PrometheusRule rule = prometheusRuleService.getById(id);
        if (rule != null) {
            PrometheusTarget target = prometheusTargetService.getById(rule.getGroupId());
            if (target != null) {
                return AjaxResult.error("请先移除端点：" + target.getJobName());
            }
        }
        boolean result = prometheusRuleService.removeById(id);
        return result ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 更新状态
     */
    @GetMapping("/syncStatus")
    @ApiOperation(value = "syncStatus")
    public void syncStatus() {
        List<PrometheusRule> prometheusRules = prometheusRuleService.list(new PrometheusRule());
        JSONObject jsonObject = prometheusApi.rules("alert");
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray groups = data.getJSONArray("groups");
        prometheusRules.forEach(prometheusRule -> {
            // 查询targets
            PrometheusTarget prometheusTarget = prometheusTargetService.getById(prometheusRule.getGroupId());
            // 如果不为空
            if (prometheusTarget != null) {
                // ruleId
                String ruleId = prometheusRule.getRuleId().toString();
                String status = ObjectUtil.defaultIfBlank(prometheusRule.getStatus(), "");
                String alertStatus = ObjectUtil.defaultIfBlank(prometheusRule.getStatus(), "");
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
                        if (labels.containsKey("id") && labels.get("id").equals(ruleId)) {
                            // 是否更新状态
                            if (!status.equals(prometheusRuleStatus)) {
                                prometheusRule.setStatus(prometheusRuleStatus);
                                isUpdate = true;
                            }
                            if (!alertStatus.equals(prometheusRuleState)) {
                                prometheusRule.setStatus(prometheusRuleState);
                                isUpdate = true;
                            }
                            if (isUpdate) {
                                System.out.println("更新：" + prometheusRule.getRuleName());
                                prometheusRuleService.updateById(prometheusRule);
                            }
                        }
                    });
                });
            }

        });
    }
}
