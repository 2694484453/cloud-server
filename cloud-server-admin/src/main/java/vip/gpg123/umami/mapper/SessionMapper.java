package vip.gpg123.umami.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vip.gpg123.umami.domain.Session;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @description 针对表【session】的数据库操作Mapper
 * @createDate 2025-12-19 03:07:34
 * @Entity vip.gpg123.umami.domain.Session
 */
@Mapper
public interface SessionMapper extends BaseMapper<Session> {

    /**
     * 统计每个country出现的次数
     *
     * @param websiteId ws
     * @return r
     */
    @Select("select distinct country,count(*) as count from session where website_id = #{websiteId} and (created_at between #{start} and #{end}) group by country;")
    List<Map<String, Object>> countByCountry(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("websiteId") String websiteId);

    /**
     * 统计每个country出现的次数
     *
     * @param websiteId ws
     * @return r
     */
    @Select("select distinct region,count(*) as count from session where website_id = #{websiteId} and country = #{country} and (created_at between #{start} and #{end}) group by region;")
    List<Map<String, Object>> countByRegion(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("websiteId") String websiteId, @Param("country") String country);
}




