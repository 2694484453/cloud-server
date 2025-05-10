package vip.gpg123.kubernetes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.kubernetes.service.UserBindClusterService;
import vip.gpg123.kubernetes.domain.UserBindCluster;
import vip.gpg123.kubernetes.mapper.UserBindClusterMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【user_bind_cluster】的数据库操作Service实现
* @createDate 2025-04-11 23:54:41
*/
@Service
public class UserBindClusterServiceImpl extends ServiceImpl<UserBindClusterMapper, UserBindCluster>
    implements UserBindClusterService {

}




