package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component("remoteShellTask")
@Slf4j
public class ALiYunPanTask implements Job {

    /**
     * 执行阿里云备份脚本
     *
     * @param hostIp 主机ip
     * @param cmd    cmd
     */
    public void backup(String hostIp, String cmd) {
        System.out.println(11);
    }

    /**
     * @param context c
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 获取任务参数
        JobDataMap dataMap = context.getMergedJobDataMap();
        String hostIp = dataMap.getString("hostIp");
        String cmd = dataMap.getString("cmd");
    }
}
