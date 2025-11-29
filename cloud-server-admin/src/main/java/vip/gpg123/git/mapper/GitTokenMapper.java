package vip.gpg123.git.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.domain.GitToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【git_access(git认证信息)】的数据库操作Mapper
 * @createDate 2025-04-29 23:42:41
 * @Entity vip.gpg123.git.domain.GitAccess
 */
@Mapper
public interface GitTokenMapper extends BaseMapper<GitToken> {

    /**
     * 根据 entity 条件，查询一条记录
     */
    GitToken one(@Param("qw") GitToken gitToken);

    /**
     * 根据 entity 条件，查询全部记录
     */
    List<GitToken> list(@Param("qw") GitToken gitToken);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<GitToken> page(@Param("page") PageDomain page, @Param("qw") GitToken gitToken);

}




