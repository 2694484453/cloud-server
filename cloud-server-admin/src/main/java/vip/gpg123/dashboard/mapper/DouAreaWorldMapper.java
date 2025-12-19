package vip.gpg123.dashboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vip.gpg123.dashboard.domain.DouAreaWorld;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author gaopuguang
 * @description 针对表【dou_area_world】的数据库操作Mapper
 * @createDate 2025-12-19 15:40:57
 * @Entity vip.gpg123.dashboard.domain.DouAreaWorld
 */
@Mapper
public interface DouAreaWorldMapper extends BaseMapper<DouAreaWorld> {

    @Select("select * from dou_area_world where parent_id = 0 and iso_code_2 = #{code2}")
    DouAreaWorld selectCountryByCode2(@Param("code2") String code2);
}




