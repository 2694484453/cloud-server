package vip.gpg123.git.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.git.domain.GitCodeSpace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.git.domain.GitToken;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【git_code_space(代码空间)】的数据库操作Mapper
* @createDate 2025-11-30 02:17:54
* @Entity vip.gpg123.git.domain.GitCodeSpace
*/
@Mapper
public interface GitCodeSpaceMapper extends BaseMapper<GitCodeSpace> {

    /**
     * 根据 entity 条件，查询一条记录
     */
    GitCodeSpace one(@Param("qw") GitCodeSpace gitCodeSpace);

    /**
     * 根据 entity 条件，查询全部记录
     */
    List<GitCodeSpace> list(@Param("qw") GitCodeSpace gitCodeSpace);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<GitCodeSpace> page(@Param("page") PageDomain page, @Param("qw") GitCodeSpace gitCodeSpace);


}




