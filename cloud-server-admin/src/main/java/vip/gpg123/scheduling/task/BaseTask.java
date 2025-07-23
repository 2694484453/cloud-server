package vip.gpg123.scheduling.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.utils.SecurityUtils;
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
    public boolean updateJob(SysJob sysJob) {
       try {
           // 执行查询
           SysJob search = sysJobService.selectJobById(sysJob.getJobId());
           sysJob.setRunTime(DateUtil.date());
           sysJob.setUpdateTime(DateUtil.date());
           sysJob.setStatus("running");
           sysJob.setUpdateBy(SecurityUtils.getUsername());
           sysJob.setRunResult(null);
           // 如果不为空
           if (ObjectUtil.isNull(search)) {
               sysJob.setStatus("notFound");
               sysJob.setRunResult("查询不到这个任务，请检查！");
               return false;
           }else {
               return sysJobService.updateJob(sysJob) > 0;
           }
       } catch (Exception e) {
           System.out.println("<UNK>" + e.getMessage());
       }
        return false;
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
