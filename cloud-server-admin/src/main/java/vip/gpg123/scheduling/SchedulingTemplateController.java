package vip.gpg123.scheduling;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.scheduling.domain.SchedulingTemplate;
import vip.gpg123.scheduling.service.SchedulingTemplateService;

import java.util.List;

@RestController
@RequestMapping("/scheduling/template")
public class SchedulingTemplateController extends BaseController {

    @Autowired
    private SchedulingTemplateService schedulingTemplateService;

    /**
     * 查询
     *
     * @param schedulingTemplate st
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(SchedulingTemplate schedulingTemplate) {
        List<SchedulingTemplate> schedulingTemplates = schedulingTemplateService.list(new LambdaQueryWrapper<SchedulingTemplate>()
                .eq(StrUtil.isNotBlank(schedulingTemplate.getJobType()), SchedulingTemplate::getJobType, schedulingTemplate.getJobType())
                .orderByDesc(SchedulingTemplate::getCreateTime)
        );
        return AjaxResult.success(schedulingTemplates);
    }

    /**
     * 分页
     *
     * @param schedulingTemplate st
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(SchedulingTemplate schedulingTemplate) {
        IPage<SchedulingTemplate> page = schedulingTemplateService.page(new Page<>(TableSupport.getPageDomain().getPageNum(), TableSupport.getPageDomain().getPageSize()), new LambdaQueryWrapper<SchedulingTemplate>()
                .eq(StrUtil.isNotBlank(schedulingTemplate.getJobType()), SchedulingTemplate::getJobType, schedulingTemplate.getJobType())
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 详情
     * @param schedulingTemplate sc
     * @return r
     */
    @GetMapping("/info")
    public AjaxResult info(SchedulingTemplate schedulingTemplate) {
        SchedulingTemplate entity = schedulingTemplateService.getOne(new LambdaQueryWrapper<SchedulingTemplate>()
                .eq(StrUtil.isNotBlank(schedulingTemplate.getJobType()), SchedulingTemplate::getJobType, schedulingTemplate.getJobType())
                .eq(ObjectUtil.isNull(schedulingTemplate.getId()), SchedulingTemplate::getId, schedulingTemplate.getId())
        );
        if (entity != null) {
            return AjaxResult.success(entity);
        }
        return AjaxResult.error();
    }
}
