package vip.gpg123.repo.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.repo.domain.HelmRepo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【helm_repo(helm仓库信息表)】的数据库操作Mapper
* @createDate 2025-06-10 00:19:25
* @Entity vip.gpg123.repo.domain.HelmRepo
*/
@Mapper
public interface HelmRepoMapper extends BaseMapper<HelmRepo> {

}




