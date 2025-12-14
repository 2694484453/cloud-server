package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.domain.PrometheusRule;
import vip.gpg123.prometheus.mapper.PrometheusRuleMapper;
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

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "alertName", required = false) String alertName,
                           @RequestParam(value = "groupName", required = false) String groupName,
                           @RequestParam(value = "type", required = false) String type) {
        List<PrometheusRule> list = prometheusRuleService.list(new LambdaQueryWrapper<PrometheusRule>()
                .eq(StrUtil.isNotBlank(alertName), PrometheusRule::getAlertName, alertName)
                .eq(StrUtil.isNotBlank(groupName), PrometheusRule::getGroupName, groupName)
                .eq(StrUtil.isNotBlank(type), PrometheusRule::getType, type)
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
    public TableDataInfo page(@RequestParam(value = "alertName", required = false) String alertName,
                              @RequestParam(value = "groupName", required = false) String groupName,
                              @RequestParam(value = "type", required = false) String type) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<PrometheusRule> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        PrometheusRule search = new PrometheusRule();
        search.setAlertName(alertName);
        search.setGroupName(groupName);
        search.setType(type);
        List<PrometheusRule> list = prometheusRuleMapper.page(pageDomain, search);
        page.setRecords(list);
        page.setTotal(prometheusRuleMapper.list(search).size());
        return PageUtils.toPageByIPage(page);
    }
}
