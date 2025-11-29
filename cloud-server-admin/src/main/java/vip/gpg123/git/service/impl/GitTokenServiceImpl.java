package vip.gpg123.git.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.git.domain.GitToken;
import vip.gpg123.git.service.GitTokenService;
import vip.gpg123.git.mapper.GitTokenMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【git_access(git认证信息)】的数据库操作Service实现
* @createDate 2025-04-29 23:42:41
*/
@Service
public class GitTokenServiceImpl extends ServiceImpl<GitTokenMapper, GitToken> implements GitTokenService {

}




