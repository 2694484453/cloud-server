package vip.gpg123.dashboard.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vip.gpg123.dashboard.domain.States;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author gaopuguang
 * @description 针对表【states】的数据库操作Mapper
 * @createDate 2025-12-19 16:33:06
 * @Entity vip.gpg123.dashboard.domain.States
 */
public interface StatesMapper extends BaseMapper<States> {

    /**
     * 根据isoCode查询
     * @param code code
     * @return r
     */
    @Select("select * from states where iso_code = #{code}")
    States selectByIsoCode(@Param("code") String code);
}




