package com.ruoyi.web.controller.build;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/11 0:18
 **/
@RestController
@RequestMapping("/job")
@Api(tags = "任务管理")
public class JobController {

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<Job> jobs = K8sUtil.createKClient().batch().v1().jobs().inAnyNamespace().list().getItems();
        return AjaxResult.success(jobs);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }

    /**
     * 查询详情
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam("name") String name) {
        Job job = K8sUtil.createKClient().batch().v1().jobs().withName(name).get();
        if (ObjectUtil.isNotEmpty(job)) {
            return AjaxResult.success(job);
        }
        return AjaxResult.success("查询成功", null);
    }

    /**
     * 创建一个job
     * @return r
     */
    @GetMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add() {
        // 执行创建
        K8sUtil.createKClient().batch().v1().jobs().inNamespace("").withName("").create();
        return null;
    }
}
