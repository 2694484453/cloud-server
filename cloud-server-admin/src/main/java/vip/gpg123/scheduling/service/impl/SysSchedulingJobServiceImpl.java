package vip.gpg123.scheduling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.gpg123.quartz.domain.SysJob;
import vip.gpg123.quartz.mapper.SysJobMapper;
import vip.gpg123.scheduling.service.SysSchedulingJobService;

@Service
public class SysSchedulingJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements SysSchedulingJobService {
}
