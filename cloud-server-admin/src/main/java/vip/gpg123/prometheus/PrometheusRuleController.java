package vip.gpg123.prometheus;

import cn.hutool.core.date.DateUtil;
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
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.prometheus.domain.PrometheusGroup;
import vip.gpg123.prometheus.domain.PrometheusRule;
import vip.gpg123.prometheus.mapper.PrometheusRuleMapper;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusGroupService;
import vip.gpg123.prometheus.service.PrometheusRuleService;

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

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(PrometheusRule prometheusRule) {
        List<PrometheusRule> list = prometheusRuleService.list(new LambdaQueryWrapper<PrometheusRule>()
                .eq(StrUtil.isNotBlank(prometheusRule.getRuleName()), PrometheusRule::getRuleName, prometheusRule.getRuleName())
                .eq(StrUtil.isNotBlank(prometheusRule.getGroupId()), PrometheusRule::getGroupId, prometheusRule.getGroupId())
                .eq(StrUtil.isNotBlank(prometheusRule.getType()), PrometheusRule::getType, prometheusRule.getType())
                .eq(PrometheusRule::getCreateBy, getUserId())
                .orderByAsc(PrometheusRule::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(PrometheusRule prometheusRule) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<PrometheusRule> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        PrometheusRule search = new PrometheusRule();
        search.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        List<PrometheusRule> list = prometheusRuleMapper.page(pageDomain, prometheusRule);
        page.setRecords(list);
        page.setTotal(prometheusRuleMapper.list(search).size());
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
        List<PrometheusRule> prometheusRules = prometheusRuleMapper.list(null);
        JSONObject jsonObject = prometheusApi.rules("alert");
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray groups = data.getJSONArray("groups");
        prometheusRules.forEach(prometheusRule -> {
            // 查询group
            PrometheusGroup prometheusGroup = prometheusGroupService.getById(prometheusRule.getGroupId());
            String alertName = prometheusRule.getRuleName();
            String groupName = prometheusGroup.getGroupName();
            String type = prometheusRule.getType();
            String status = ObjectUtil.defaultIfBlank(prometheusRule.getStatus(), "");
            String alertStatus = ObjectUtil.defaultIfBlank(prometheusRule.getStatus(), "");
            groups.forEach(group -> {
                JSONObject groupJsonObject = JSONUtil.parseObj(group);
                String prometheusGroupName = groupJsonObject.getStr("name");
                JSONArray rules = groupJsonObject.getJSONArray("rules");
                rules.forEach(rule -> {
                    boolean isUpdate = false;
                    JSONObject ruleJsonObject = JSONUtil.parseObj(rule);
                    String prometheusRuleName = ruleJsonObject.getStr("name");
                    String prometheusRuleStatus = ruleJsonObject.getStr("health");
                    String prometheusRuleState = ruleJsonObject.getStr("state");
                    if (groupName.equals(prometheusGroupName) && alertName.equals(prometheusRuleName)) {
                        if (!status.equals(prometheusRuleStatus)) {
                            prometheusRule.setStatus(prometheusRuleStatus);
                            isUpdate = true;
                        }
                        if (!alertStatus.equals(prometheusRuleState)) {
                            prometheusRule.setStatus(prometheusRuleState);
                            isUpdate = true;
                        }
                    }
                    if (isUpdate) {
                        prometheusRuleService.updateById(prometheusRule);
                    }
                });
            });
        });
    }
}
