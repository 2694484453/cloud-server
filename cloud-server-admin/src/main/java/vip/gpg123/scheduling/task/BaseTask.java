package vip.gpg123.scheduling.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.constant.Constants;
import vip.gpg123.common.constant.ScheduleConstants;
import vip.gpg123.common.exception.job.TaskException;
import vip.gpg123.common.utils.ExceptionUtil;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.common.utils.StringUtils;
import vip.gpg123.common.utils.bean.BeanUtils;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.domain.SysJobLog;
import vip.gpg123.quartz.service.ISysJobLogService;
import vip.gpg123.quartz.service.ISysJobService;
import vip.gpg123.quartz.util.JobInvokeUtil;

import java.util.Date;

@Component
@Slf4j
public abstract class BaseTask implements Job {

    @Autowired
    private ISysJobService sysJobService;

    @Autowired
    private ISysJobLogService sysJobLogService;

    /**
     * 线程本地变量
     */
    private static final ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    /**
     * 执行
     * @param context c
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysJob sysJob = new SysJob();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        Long taskId = sysJob.getJobId();
        TaskContext.setCurrentTaskId(taskId);
        try {
            before(context, sysJob);
            // 执行业务方法（通过JobDataMap传递）
            JobInvokeUtil.invokeMethod(sysJob);
            //
            after(context, sysJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            try {
                after(context, sysJob, e);
            } catch (SchedulerException | TaskException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void before(JobExecutionContext context, SysJob sysJob) throws SchedulerException, TaskException {
        threadLocal.set(new Date());
        // 设置任务为执行状态
        sysJob.setStatus("running");
        // 更新
        SpringUtils.getBean(ISysJobService.class).updateJob(sysJob);
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void after(JobExecutionContext context, SysJob sysJob, Exception e) throws SchedulerException, TaskException {
        Date startTime = threadLocal.get();
        threadLocal.remove();
        sysJob = sysJobService.selectJobById(sysJob.getJobId());
        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setStartTime(startTime);
        sysJobLog.setStopTime(new Date());
        sysJobLog.setResultInfo(sysJob.getRunResult());
        long runMs = sysJobLog.getStopTime().getTime() - sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            sysJobLog.setStatus(Constants.FAIL_TAG);
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        } else {
            sysJobLog.setStatus(Constants.SUCCESS_TAG);
        }

        // 结果日志写入数据库当中
        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
        TaskContext.clear();
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;

    /**
     * 更新任务
     * @param sysJob 任务
     */
    public void updateJob(SysJob sysJob) {
       try {
           // 执行检查
           SysJob search = sysJobService.selectJobById(sysJob.getJobId());
           sysJob.setRunTime(DateUtil.date());
           sysJob.setUpdateTime(DateUtil.date());
           sysJob.setStatus("running");
           sysJob.setUpdateBy(SecurityUtils.getUsername());
           sysJob.setRunResult(null);
           // 如果为空
           if (ObjectUtil.isNull(search)) {
               sysJob.setStatus("notFound");
               sysJob.setRunResult("查询不到这个任务，请检查！");
           }else {
               sysJobService.updateById(sysJob);
           }
       } catch (Exception e) {
           System.out.println("<UNK>" + e.getMessage());
       }
    }

    /**
     * 保存日志
     * @param sysJobLog 对象
     */
    public void saveJobLogs(SysJobLog sysJobLog) {
        // 保存日志到数据库
        sysJobLog.setCreateBy(SecurityUtils.getUsername());
        sysJobLog.setCreateTime(DateUtil.date());
        sysJobLogService.save(sysJobLog);
    }

    /**
     * 更新日志
     * @param sysJobLog 对象
     */
    public void updateJobLogs(SysJobLog sysJobLog) {
        // 更新日志到数据库
        sysJobLog.setUpdateBy(SecurityUtils.getUsername());
        sysJobLog.setUpdateTime(DateUtil.date());
        sysJobLogService.updateById(sysJobLog);
    }

    /**
     * 保存或更新日志
     * @param sysJobLog 对象
     */
    public void saveOrUpdateJobLogs(SysJobLog sysJobLog) {
        // 保存或更新日志
        sysJobLog.setCreateBy(SecurityUtils.getUsername());
        sysJobLog.setUpdateBy(SecurityUtils.getUsername());
        sysJobLogService.saveOrUpdate(sysJobLog);
    }

    /**
     * 删除日志
     * @param logId id
     */
    public void deleteJobLogs(Long logId) {
        // 删除日志
        sysJobLogService.deleteJobLogById(logId);
    }

}
