package vip.gpg123.scheduling.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.scheduling.domain.SchedulingTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【scheduling_template(调度模版)】的数据库操作Mapper
* @createDate 2025-12-14 21:33:02
* @Entity vip.gpg123.scheduling.domain.SchedulingTemplate
*/

@Mapper
public interface SchedulingTemplateMapper extends BaseMapper<SchedulingTemplate> {

}




