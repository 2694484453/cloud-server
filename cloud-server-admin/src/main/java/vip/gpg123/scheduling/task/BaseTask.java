package vip.gpg123.scheduling.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.exception.job.TaskException;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.domain.SysJobLog;
import vip.gpg123.quartz.service.ISysJobLogService;
import vip.gpg123.quartz.service.ISysJobService;

@Component
public abstract class BaseTask {

    @Autowired
    private ISysJobService sysJobService;

    @Autowired
    private ISysJobLogService sysJobLogService;

    /**
     * 更新任务
     * @param sysJob 任务
     */
    public void updateJob(SysJob sysJob) {
       try {
           // 执行查询
           SysJob search = sysJobService.selectJobById(sysJob.getJobId());
           sysJob.setRunTime(DateUtil.date());
           // 如果不为空
           if (ObjectUtil.isNotNull(search)) {
               sysJob.setStatus("running");
           } else {
               sysJob.setStatus("notFound");
               sysJob.setRunResult("查询不到这个任务，请检查！");
           }
           sysJobService.updateJob(sysJob);
       } catch (SchedulerException | TaskException e) {
           sysJob.setStatus("error");
           sysJob.setRunResult(e.getMessage());
       }
    }

    /**
     * 保存日志
     * @param sysJobLog 对象
     */
    public void saveJobLogs(SysJobLog sysJobLog) {
        // 保存日志到数据库
        sysJobLogService.save(sysJobLog);
    }

    /**
     * 更新日志
     * @param sysJobLog 对象
     */
    public void updateJobLogs(SysJobLog sysJobLog) {
        // 更新日志到数据库
        sysJobLogService.updateById(sysJobLog);
    }

    /**
     * 保存或更新日志
     * @param sysJobLog 对象
     */
    public void saveOrUpdateJobLogs(SysJobLog sysJobLog) {
        // 保存或更新日志
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
