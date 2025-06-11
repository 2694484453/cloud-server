package vip.gpg123.kubernetes.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vip.gpg123.kubernetes.domain.KubernetesCluster;

import java.io.File;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KubernetesClusterVo extends KubernetesCluster implements Serializable {

    private File file;

}
