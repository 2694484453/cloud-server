package vip.gpg123.scheduling.task;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import vip.gpg123.quartz.domain.SysJob;

@Component("localShellTask")
public class HelmRepoTask extends BaseTask {

    /**
     * 本地执行helm仓库更新
     * @param cmd   命令
     */
    public void helmRepoUpdate(String cmd) {

    }


    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {

    }
}
