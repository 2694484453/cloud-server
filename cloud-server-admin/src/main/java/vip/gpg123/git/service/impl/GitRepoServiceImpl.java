package vip.gpg123.git.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.service.GitRepoService;
import vip.gpg123.git.mapper.GitRepoMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【git_repo】的数据库操作Service实现
* @createDate 2025-11-20 00:02:45
*/
@Service
public class GitRepoServiceImpl extends ServiceImpl<GitRepoMapper, GitRepo>
    implements GitRepoService{

}




