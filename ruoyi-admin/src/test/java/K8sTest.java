import com.ruoyi.common.utils.K8sUtil;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.api.model.ListOptionsBuilder;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import io.fabric8.openshift.client.OpenShiftClient;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:51
 **/
public class K8sTest {

    @Test
    public void t1() {
        KubernetesClient client = K8sUtil.createKClient();
        ConfigMap configMap = client.configMaps().inNamespace("kube-system").withName("coredns-custom").get();
        List<ConfigMap> list = client.configMaps().inNamespace("kube-system").list().getItems();
        System.out.println(client);
    }

    @Test
    public void t2() {
        KubernetesClient client = K8sUtil.createKClient();
        NamespaceList namespaceList = client.namespaces().list();
        //System.out.println(namespaceList);
        ListOptions listOptions = new ListOptionsBuilder()
                .withKind("IngressRoute")
                .withApiVersion("v1alpha1")
                .build();
        CustomResourceDefinitionList list = client.apiextensions().v1().customResourceDefinitions().list(listOptions);
        list.getItems().forEach(System.out::println);
    }

    @Test
    public void t3() {
        OpenShiftClient openShiftClient = K8sUtil.createOClient();
        //MixedOperation<>   = openShiftClient.cr();
    }

    @Test
    public void  t4(){
        String path = "D:\\Project\\docker-compose\\k3s\\k3s.gpg123.vip";
        try {
            List<Config> clusters = new ArrayList<>();
            Config config = KubeConfigUtils.parseConfig(new File(path));
            clusters.add(config);
            System.out.println(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
