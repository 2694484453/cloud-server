package vip.gpg123.devops.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.devops.domain.DevopsCronJob;
import vip.gpg123.devops.service.DevopsCronJobService;
import vip.gpg123.devops.mapper.DevopsCronJobMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【devops_cron_job(定时任务)】的数据库操作Service实现
* @createDate 2025-05-08 01:58:32
*/
@Service
public class DevopsCronJobServiceImpl extends ServiceImpl<DevopsCronJobMapper, DevopsCronJob> implements DevopsCronJobService{

}




