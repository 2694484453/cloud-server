package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("remoteShellTask")
@Slf4j
public class ALiYunPanTask {

    /**
     * 执行阿里云备份脚本
     *
     * @param hostIp 主机ip
     * @param cmd    cmd
     */
    public void backup(String hostIp, String cmd) {
        System.out.println(11);
    }

}
