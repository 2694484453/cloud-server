package vip.gpg123.quartz.util;

import java.lang.reflect.Method;
import java.util.Date;

import cn.hutool.core.util.ObjectUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.gpg123.common.constant.ScheduleConstants;
import vip.gpg123.common.exception.job.TaskException;
import vip.gpg123.common.utils.ExceptionUtil;
import vip.gpg123.common.utils.StringUtils;
import vip.gpg123.common.utils.bean.BeanUtils;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.domain.SysJobLog;
import vip.gpg123.quartz.service.ISysJobLogService;
import vip.gpg123.quartz.service.ISysJobService;

/**
 * 抽象quartz调用
 *
 * @author gpg123
 */
public abstract class AbstractQuartzJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static final ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysJob sysJob = new SysJob();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            logger.info("开始执行:{}", sysJob.getJobName());
            // 之前
            before(context, sysJob);
            // bean
            String beanName = sysJob.getJobClass();
            // 方法名
            String methodName = sysJob.getJobMethod();
            // 参数
            Object params = sysJob.getJobParams();
            Object bean = SpringUtils.getBean(beanName);
            Method method = bean.getClass().getMethod(methodName);
            // 判断是否有参方法
            if (ObjectUtil.isNotEmpty(params)) {
                logger.info("调用有参方法:{}", params);
                // 有参数调用
                method.invoke(bean, params);
            } else {
                logger.info("调用无参方法:{}", params);
                //无参数调用
                method.invoke(bean);
            }
//            doExecute(context, sysJob);
            // 之后
            after(context, sysJob, null);
        } catch (Exception e) {
            logger.error("任务执行异常  - ：", e);
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

        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getJobClass() + "." + sysJob.getJobMethod() + "(" + sysJob.getJobParams() + ")");
        sysJobLog.setStartTime(startTime);
        sysJobLog.setStopTime(new Date());
        sysJobLog.setResultInfo(sysJob.getRunResult());
        long runMs = sysJobLog.getStopTime().getTime() - sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            sysJobLog.setStatus("fail");
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
            sysJob.setRunStatus("error");
        } else {
            sysJob.setRunStatus("done");
        }
        sysJob.setStatus("");
        sysJobLog.setStatus(sysJob.getRunStatus());
        // 更新状态
        SpringUtils.getBean(ISysJobService.class).updateJob(sysJob);
        // 结果日志写入数据库当中
        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;
}
