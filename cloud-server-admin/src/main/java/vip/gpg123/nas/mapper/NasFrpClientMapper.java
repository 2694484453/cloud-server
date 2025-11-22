package vip.gpg123.nas.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.nas.domain.NasFrpClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【nas_frp_client(frp客户端配置信息表)】的数据库操作Mapper
* @createDate 2025-05-10 17:40:35
* @Entity vip.gpg123.nas.domain.NasFrpClient
*/
@Mapper
public interface NasFrpClientMapper extends BaseMapper<NasFrpClient> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    NasFrpClient selectOne(@Param("qw") NasFrpClient nasFrpClient);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<NasFrpClient> selectList(@Param("qw") NasFrpClient nasFrpClient);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<NasFrpClient> page(@Param("page") PageDomain page, @Param("qw") NasFrpClient nasFrpClient);
}




