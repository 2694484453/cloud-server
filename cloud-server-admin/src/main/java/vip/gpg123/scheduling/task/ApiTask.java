package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.nas.NasFrpClientController;

@Component("apiTask")
@Slf4j
public class ApiTask  {

    public void syncNasFrpClientStatus(){
        SpringUtils.getBean(NasFrpClientController.class).sync();
    }

}
