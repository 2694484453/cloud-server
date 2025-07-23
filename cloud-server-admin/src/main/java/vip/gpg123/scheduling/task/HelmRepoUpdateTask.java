package vip.gpg123.scheduling.task;

import org.springframework.stereotype.Component;

@Component("localShellTask")
public class HelmRepoUpdateTask extends ShellBaseTask{

    /**
     * 本地执行helm仓库更新
     *
     * @param jobId 任务ID
     * @param cmd   命令
     */
    @Override
    public void runLocalShell(Long jobId, String cmd) {
        super.runLocalShell(jobId, cmd);
    }
}
