package vip.gpg123.devops.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.devops.domain.DevopsJob;
import vip.gpg123.devops.service.DevopsJobService;
import vip.gpg123.devops.mapper.DevopsJobMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【devops_job(普通任务)】的数据库操作Service实现
* @createDate 2025-05-08 01:57:47
*/
@Service
public class DevopsJobServiceImpl extends ServiceImpl<DevopsJobMapper, DevopsJob> implements DevopsJobService{

}




