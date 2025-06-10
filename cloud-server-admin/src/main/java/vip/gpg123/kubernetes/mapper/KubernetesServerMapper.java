package vip.gpg123.kubernetes.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【kubernetes_server(k8s服务主机信息表)】的数据库操作Mapper
* @createDate 2025-05-10 16:41:24
* @Entity vip.gpg123.kubernetes.domain.KubernetesServer
*/
@Mapper
public interface KubernetesServerMapper extends BaseMapper<KubernetesCluster> {

}




