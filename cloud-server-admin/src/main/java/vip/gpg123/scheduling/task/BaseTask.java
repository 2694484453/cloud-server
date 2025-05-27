package vip.gpg123.scheduling.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.quartz.domain.SysJobLog;
import vip.gpg123.quartz.service.ISysJobLogService;
import vip.gpg123.vps.domain.CloudHostServer;
import vip.gpg123.vps.service.CloudHostServerService;

import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

@Component
@Slf4j
public abstract class BaseTask {

    @Autowired
    private CloudHostServerService cloudHostServerService;

    @Autowired
    private ISysJobLogService sysJobLogService;

    private static final String remote_shell = "remoteShell";

    /**
     * 远程执行shell
     * @param jobId 任务ID
     * @param hostIp 主机IP
     * @param cmd 命令
     */
    public void runRemoteShell(String jobId, String hostIp, String cmd) {
        log.info("执行{}类型定时任务: {}", remote_shell ,jobId);
        // 查询主机
        CloudHostServer cloudHostServer = cloudHostServerService.getById(hostIp);
        // 设置日志
        SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setCreateBy("sys-job");
        sysJobLog.setJobName(sysJobLog.getJobName());
        sysJobLog.setJobGroup(sysJobLog.getJobGroup());
        sysJobLog.setInvokeTarget(sysJobLog.getInvokeTarget());
        sysJobLog.setCreateTime(DateUtil.date());
        // 判空
        if (ObjectUtil.isNotNull(cloudHostServer)) {
            try {
                Session session = JschUtil.createSession(cloudHostServer.getHostIp(), cloudHostServer.getPort(), cloudHostServer.getUserName(), cloudHostServer.getPassWord());
                session.connect();
                String res = JschUtil.exec(session, cmd, StandardCharsets.UTF_8);
                sysJobLog.setStatus("success");
                sysJobLog.setResultInfo(res);
            } catch (Exception e) {
                sysJobLog.setStatus("fail");
                sysJobLog.setExceptionInfo(e.getMessage());
                throw new RuntimeException(e);
            } finally {
                // 保存日志
                AsyncManager.me().execute(new TimerTask() {
                    @Override
                    public void run() {
                        sysJobLogService.save(sysJobLog);
                    }
                });
            }
        }
    }
}
