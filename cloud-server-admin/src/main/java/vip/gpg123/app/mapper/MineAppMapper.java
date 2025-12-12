package vip.gpg123.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.app.domain.MineApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.quartz.domain.SysJob;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【helm_app】的数据库操作Mapper
* @createDate 2025-04-27 23:35:55
* @Entity vip.gpg123.app.domain.HelmApp
*/
@Mapper
public interface MineAppMapper extends BaseMapper<MineApp> {
    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    MineApp one(@Param("qw") MineApp mineApp);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<MineApp> list(@Param("qw") MineApp mineApp);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<MineApp> page(@Param("page") PageDomain page, @Param("qw") MineApp mineApp);
}




