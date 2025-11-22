package vip.gpg123.kubernetes.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.vps.domain.CloudHostServer;

import java.util.List;

/**
* @author Administrator
* @description 针对表【kubernetes_server(k8s服务主机信息表)】的数据库操作Mapper
* @createDate 2025-05-10 16:41:24
* @Entity vip.gpg123.kubernetes.domain.KubernetesServer
*/
@Mapper
public interface KubernetesClusterMapper extends BaseMapper<KubernetesCluster> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    KubernetesCluster one(@Param("qw") KubernetesCluster kubernetesCluster);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<KubernetesCluster> list(@Param("qw") KubernetesCluster kubernetesCluster);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<KubernetesCluster> page(@Param("page") PageDomain page, @Param("qw") KubernetesCluster kubernetesCluster);
}




