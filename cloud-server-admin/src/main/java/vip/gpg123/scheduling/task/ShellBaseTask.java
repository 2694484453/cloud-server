package vip.gpg123.scheduling.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.service.ISysJobService;
import vip.gpg123.vps.domain.CloudHostServer;
import vip.gpg123.vps.service.CloudHostServerService;

import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

@Component
@Slf4j
public class ShellBaseTask extends BaseTask {

    @Autowired
    private CloudHostServerService cloudHostServerService;

    @Autowired
    private ISysJobService sysJobService;

    private static final String remote_shell = "remoteShell";

    private static final String local_shell = "remoteShell";

    /**
     * 远程执行shell
     *
     * @param jobId  任务ID
     * @param hostIp 主机IP
     * @param cmd    命令
     */
    public void runRemoteShell(Long jobId, String hostIp, String cmd) {
        log.info("执行{}类型定时任务: {}", remote_shell, jobId);
        // 查询到任务
        if (ObjectUtil.isNotNull(jobId)) {
            SysJob sysJob = sysJobService.selectJobById(jobId);
            if (ObjectUtil.isNotNull(sysJob)) {
                super.updateJob(sysJob);
                // 查询主机
                CloudHostServer cloudHostServer = cloudHostServerService.getById(hostIp);
                // 判空
                if (ObjectUtil.isNotNull(cloudHostServer)) {
                    try {
                        Session session = JschUtil.createSession(cloudHostServer.getHostIp(), cloudHostServer.getPort(), cloudHostServer.getUserName(), cloudHostServer.getPassWord());
                        session.connect();
                        String res = JschUtil.exec(session, cmd, StandardCharsets.UTF_8);
                        sysJob.setRunResult(res);
                        sysJob.setStatus("success");
                    } catch (Exception e) {
                        sysJob.setRunResult(e.getMessage());
                        sysJob.setStatus("fail");
                    } finally {
                        // 执行异步任务
                        AsyncManager.me().execute(new TimerTask() {
                            @Override
                            public void run() {
                                // 更新job状态
                                ShellBaseTask.super.updateJob(sysJob);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 本地执行shell
     *
     * @param jobId 任务ID
     * @param cmd   命令
     */
    public void runLocalShell(Long jobId, String cmd) {
        log.info("执行{}类型定时任务: {}", local_shell, jobId);
        // 查询到任务
        if (ObjectUtil.isNotNull(jobId)) {
            SysJob sysJob = sysJobService.selectJobById(jobId);
            if (ObjectUtil.isNotNull(sysJob)) {
                super.updateJob(sysJob);
                try {
                    String res = RuntimeUtil.execForStr(StandardCharsets.UTF_8, cmd);
                    sysJob.setRunResult(res);
                    sysJob.setStatus("success");
                } catch (Exception e) {
                    sysJob.setRunResult(e.getMessage());
                    sysJob.setStatus("fail");
                } finally {
                    // 执行异步任务
                    AsyncManager.me().execute(new TimerTask() {
                        @Override
                        public void run() {
                            // 更新job状态
                            ShellBaseTask.super.updateJob(sysJob);
                        }
                    });
                }
            }
        }
    }
}
