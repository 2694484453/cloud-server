package vip.gpg123.dashboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.dashboard.domain.Session;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【session】的数据库操作Mapper
* @createDate 2025-12-19 03:07:34
* @Entity vip.gpg123.dashboard.domain.Session
*/
@Mapper
public interface SessionMapper extends BaseMapper<Session> {

}




