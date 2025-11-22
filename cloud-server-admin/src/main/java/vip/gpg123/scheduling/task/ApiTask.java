package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.nas.NasFrpClientController;
import vip.gpg123.quartz.domain.SysJob;

@Component("apiTask")
@Slf4j
public abstract class ApiTask extends BaseTask {

    @Autowired
    private NasFrpClientController  nasFrpClientController;


    /**
     * 执行
     *
     * @param context c
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
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
        nasFrpClientController.sync();
    }
}
