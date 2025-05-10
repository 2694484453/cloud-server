package vip.gpg123.kubernetes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.kubernetes.domain.KubernetesServer;
import vip.gpg123.kubernetes.service.KubernetesServerService;
import vip.gpg123.kubernetes.mapper.KubernetesServerMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【kubernetes_server(k8s服务主机信息表)】的数据库操作Service实现
* @createDate 2025-05-10 16:41:24
*/
@Service
public class KubernetesServerServiceImpl extends ServiceImpl<KubernetesServerMapper, KubernetesServer>
    implements KubernetesServerService{

}




