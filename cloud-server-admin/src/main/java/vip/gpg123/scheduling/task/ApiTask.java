package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.nas.NasFrpClientController;

@Component("apiTask")
@Slf4j
public abstract class ApiTask implements Job {

    @Autowired
    private NasFrpClientController  nasFrpClientController;


    public void syncNasFrpClientStatus(){
        nasFrpClientController.sync();
    }
}
