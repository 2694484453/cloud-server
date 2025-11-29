package vip.gpg123.scheduling;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.annotation.Log;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.enums.BusinessType;
import vip.gpg123.common.exception.job.TaskException;
import vip.gpg123.common.utils.PageUtils;

import vip.gpg123.common.utils.poi.ExcelUtil;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.service.ISysJobService;
import vip.gpg123.quartz.util.CronUtils;
import vip.gpg123.scheduling.mapper.SysSchedulingJobMapper;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调度任务信息操作处理
 *
 * @author gpg123
 */
@RestController
@RequestMapping("/scheduling/job")
@Api(tags = "调度任务信息")
public class SysSchedulingJobController extends BaseController {

    @Autowired
    private ISysJobService jobService;

    @Autowired
    private SysSchedulingJobMapper schedulingJobMapper;

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public TableDataInfo list(SysJob sysJob)
    {
        List<SysJob> list = jobService.list(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUsername())
                .like(StrUtil.isNotBlank(sysJob.getJobName()), SysJob::getJobName, sysJob.getJobName())
                .like(StrUtil.isNotBlank(sysJob.getJobGroup()), SysJob::getJobGroup, sysJob.getJobGroup())
                .like(StrUtil.isNotBlank(sysJob.getStatus()), SysJob::getStatus, sysJob.getStatus())
                .like(StrUtil.isNotBlank(sysJob.getConcurrent()), SysJob::getConcurrent, sysJob.getConcurrent())
                .like(StrUtil.isNotBlank(sysJob.getStatus()), SysJob::getStatus, sysJob.getStatus())
                .orderByDesc(SysJob::getCreateTime));
        return getDataTable(list);
    }

    /**
     * 分页查询
     * @param sysJob 参数
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(SysJob sysJob) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<SysJob> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        SysJob search = new SysJob();
        search.setJobName(sysJob.getJobName());
        search.setJobGroup(sysJob.getJobGroup());
        search.setStatus(sysJob.getStatus());
        search.setCreateBy(String.valueOf(getUserId()));
        List<SysJob> list = schedulingJobMapper.page(pageDomain, search);
        page.setRecords(list);
        page.setTotal(schedulingJobMapper.list(search).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 导出定时任务列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob)
    {
        List<SysJob> list = jobService.selectJobList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        util.exportExcel(response, list, "定时任务");
    }

    /**
     * 获取定时任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId)
    {
        return success(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:add')")
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        job.setCreateBy(String.valueOf(getUserId()));
        return toAjax(jobService.insertJob(job));
    }

    /**
     * 修改定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:edit')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        job.setUpdateBy(String.valueOf(getUserId()));
        return toAjax(jobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    //@PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException
    {
        boolean result = jobService.run(job);
        return result ? success() : error("任务不存在或已过期！");
    }

    /**
     * 删除定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException
    {
        jobService.deleteJobByIds(jobIds);
        return success();
    }

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    public AjaxResult overView() {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> jobTotal = new HashMap<>();
        jobTotal.put("title","我的任务总数");
        jobTotal.put("count",jobService.count(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUserId())
        ));

        Map<String,Object> jobRunning = new HashMap<>();
        jobRunning.put("title","运行中");
        jobRunning.put("count",jobService.count(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUserId())
                .eq(SysJob::getStatus, "running")
        ));

        Map<String,Object> jobPause = new HashMap<>();
        jobPause.put("title","未运行");
        jobPause.put("count",jobService.count(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getCreateBy, getUserId())
                .ne(SysJob::getStatus, "running")
        ));

        list.add(jobTotal);
        list.add(jobRunning);
        list.add(jobPause);
        return AjaxResult.success(list);
    }
}
