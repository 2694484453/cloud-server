package vip.gpg123.git.mapper;

import vip.gpg123.git.domain.GitRepo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【git】的数据库操作Mapper
* @createDate 2024-12-01 01:00:54
* @Entity domain.git.vip.gpg.Git
*/
@Mapper
public interface GitRepoMapper extends BaseMapper<GitRepo> {

}




