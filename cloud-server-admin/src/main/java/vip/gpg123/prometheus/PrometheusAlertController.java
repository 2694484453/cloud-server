package vip.gpg123.prometheus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.prometheus.domain.PrometheusAlert;
import vip.gpg123.prometheus.dto.PrometheusAlertVO;
import vip.gpg123.prometheus.service.PrometheusAlertService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/prometheus/alert")
public class PrometheusAlertController extends BaseController {

    @Autowired
    private PrometheusAlertService prometheusAlertService;


    /**
     * list
     *
     * @param prometheusAlert pl
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(PrometheusAlert prometheusAlert) {
        List<PrometheusAlert> alerts = prometheusAlertService.list(prometheusAlert);
        return AjaxResult.success(alerts);
    }

    /**
     * page
     *
     * @param prometheusAlert pl
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(Page<PrometheusAlert> prometheusAlertPage, PrometheusAlert prometheusAlert) {
        prometheusAlert.setCreateBy(SecurityUtils.getUserId().toString());
        IPage<PrometheusAlertVO> page = prometheusAlertService.pageExtension(prometheusAlertPage, prometheusAlert);
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 删除
     *
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestParam("id") String id) {
        boolean res = prometheusAlertService.removeById(id);
        return res ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }
}
