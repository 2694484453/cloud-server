package vip.gpg123.dashboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vip.gpg123.dashboard.domain.WebsiteEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
* @author gaopuguang
* @description 针对表【website_event】的数据库操作Mapper
* @createDate 2025-12-19 03:49:53
* @Entity vip.gpg123.dashboard.domain.WebsiteEvent
*/
@Mapper
public interface WebsiteEventMapper extends BaseMapper<WebsiteEvent> {

    @Select("SELECT count(distinct visit_id) FROM website_event WHERE website_id = #{websiteId} and (created_at between #{start} and #{end})")
    int visits(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("websiteId") String websiteId);

}




