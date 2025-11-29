package vip.gpg123.common.utils;

import org.quartz.JobExecutionContext;
import vip.gpg123.system.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author gpg123
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
