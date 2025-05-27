package vip.gpg123.scheduling.task;

import org.springframework.stereotype.Component;

/**
 * 远程命令执行
 */
@Component("remoteShellTask")
public class RemoteShellTask extends BaseTask {

    /**
     * 执行阿里云备份脚本
     * @param jobId 任务id
     * @param hostIp 主机ip
     * @param cmd cmd
     */
    @Override
    public void runRemoteShell(String jobId, String hostIp, String cmd) {
        super.runRemoteShell(jobId, hostIp, cmd);
    }
}
