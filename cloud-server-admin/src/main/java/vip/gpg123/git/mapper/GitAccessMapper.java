package vip.gpg123.git.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.git.domain.GitAccess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【git_access(git认证信息)】的数据库操作Mapper
* @createDate 2025-04-29 23:42:41
* @Entity vip.gpg123.git.domain.GitAccess
*/
@Mapper
public interface GitAccessMapper extends BaseMapper<GitAccess> {

}




