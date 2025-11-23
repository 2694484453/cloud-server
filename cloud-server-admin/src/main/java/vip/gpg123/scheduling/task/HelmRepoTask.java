package vip.gpg123.scheduling.task;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import vip.gpg123.quartz.domain.SysJob;

@Component("localShellTask")
public class HelmRepoTask {

    /**
     * 本地执行helm仓库更新
     * @param cmd   命令
     */
    public void helmRepoUpdate(String cmd) {

    }


}
