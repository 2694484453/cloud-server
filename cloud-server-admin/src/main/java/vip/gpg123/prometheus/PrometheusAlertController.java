package vip.gpg123.prometheus;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import vip.gpg123.prometheus.domain.PrometheusAlert;
import vip.gpg123.prometheus.mapper.PrometheusAlertMapper;
import vip.gpg123.prometheus.service.PrometheusAlertService;

import java.util.List;

@RestController
@RequestMapping("/prometheus/alert")
public class PrometheusAlertController extends BaseController {

    @Autowired
    private PrometheusAlertService prometheusAlertService;

    @Autowired
    private PrometheusAlertMapper prometheusAlertMapper;

    /**
     * list
     *
     * @param prometheusAlert pl
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(PrometheusAlert prometheusAlert) {
        List<PrometheusAlert> alerts = prometheusAlertService.list(new LambdaQueryWrapper<PrometheusAlert>()
                .eq(StrUtil.isNotBlank(prometheusAlert.getGroupName()), PrometheusAlert::getGroupName, prometheusAlert.getGroupName())
                .eq(StrUtil.isNotBlank(prometheusAlert.getAlertName()), PrometheusAlert::getAlertName, prometheusAlert.getAlertName())
                .eq(StrUtil.isNotBlank(prometheusAlert.getAlertLevel()), PrometheusAlert::getAlertLevel, prometheusAlert.getAlertLevel())
                .eq(StrUtil.isNotBlank(prometheusAlert.getType()), PrometheusAlert::getType, prometheusAlert.getType())
                .eq(ObjectUtil.isNotNull(prometheusAlert.getId()), PrometheusAlert::getId, prometheusAlert.getId())
        );
        return AjaxResult.success(prometheusAlertService.list());
    }

    /**
     * page
     *
     * @param prometheusAlert pl
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(PrometheusAlert prometheusAlert) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<PrometheusAlert> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());
        List<PrometheusAlert> list = prometheusAlertMapper.page(pageDomain, prometheusAlert);
        page.setTotal(prometheusAlertMapper.list(prometheusAlert).size());
        page.setRecords(list);
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestParam("id") String id) {
        boolean res = prometheusAlertService.removeById(id);
        return res ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }
}
