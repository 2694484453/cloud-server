package vip.gpg123.scheduling.task;

import org.springframework.stereotype.Component;

@Component("localShellTask")
public class HelmRepoTask extends ShellBaseTask{

    /**
     * 本地执行helm仓库更新
     *
     * @param jobId 任务ID
     * @param cmd   命令
     */
    public void helmRepoUpdate(Long jobId, String cmd) {
        super.runLocalShell(jobId, cmd);
    }
}
