package vip.gpg123.scheduling.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.domain.SysJobLog;
import vip.gpg123.vps.domain.CloudHostServer;
import vip.gpg123.vps.service.CloudHostServerService;

import java.nio.charset.StandardCharsets;

/**
 * 远程命令执行
 */
@Slf4j
@Component
public class RemoteShellBaseTask extends BaseTask {

    @Autowired
    private CloudHostServerService cloudHostServerService;

    private static final String remote_shell = "remoteShell";

    private String hostIp = "";

    private String cmd = "";

    public RemoteShellBaseTask(String hostIp, String cmd) {
        this.hostIp = hostIp;
        this.cmd = cmd;
    }

    public RemoteShellBaseTask() {
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
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
        Long taskId = sysJob.getJobId();
        log.info("执行{}类型定时任务,{}", remote_shell, taskId);
            // 查询主机
            CloudHostServer cloudHostServer = cloudHostServerService.getById(hostIp);
            // 判空
            if (ObjectUtil.isNotNull(cloudHostServer)) {
                try {
                    Session session = JschUtil.createSession(cloudHostServer.getHostIp(), cloudHostServer.getPort(), cloudHostServer.getUserName(), cloudHostServer.getPassWord());
                    session.connect();
                    String res = JschUtil.exec(session, cmd, StandardCharsets.UTF_8);
                    sysJob.setRunResult(res);
                } catch (Exception e) {
                    sysJob.setRunResult(e.getMessage());
                } finally {
                    super.updateJob(sysJob);
                    super.saveJobLogs(new SysJobLog());
                }
            }
    }
}
