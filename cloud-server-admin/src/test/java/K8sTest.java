import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.api.model.ListOptionsBuilder;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRule;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRuleList;
import io.fabric8.openshift.client.OpenShiftClient;
import org.junit.Test;
import vip.gpg123.common.utils.K8sUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:51
 **/
public class K8sTest {

    private static final String filePath = "D:\\Project\\docker-compose\\k3s\\k3s.gpg123.vip";

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
    public void t4() {
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

    @Test
    public void t6() {
        OpenShiftClient openShiftClient = K8sUtil.createOClient();
        List<PrometheusRule> prometheusRules;
        try {
            PrometheusRuleList prometheusRuleList = openShiftClient.monitoring().prometheusRules().inAnyNamespace().list();
            prometheusRules = prometheusRuleList.getItems();
            System.out.println(prometheusRules);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void t7() {
        KubernetesClient client = K8sUtil.createKClient();
        try {
            List<Job> jobs = client.batch().v1().jobs().inNamespace("default").list().getItems();

            System.out.println(jobs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void t8() {
        String file = "D:/log.txt";
        OutputStream outputStream = FileUtil.getOutputStream(file);
        InputStream inputStream = null;
        KubernetesClient client = K8sUtil.createKClient();
        String logs = client.batch().v1().jobs().inNamespace("default").withName("my-server").getLog(true);
        LogWatch watch = client.batch().v1().jobs().inNamespace("default").withName("my-server").watchLog(outputStream);
        try {
            inputStream = watch.getOutput();
            watch.wait();
            IoUtil.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(outputStream);
            IoUtil.close(inputStream);
        }
        System.out.println(logs);
    }

    @Test
    public void t9() {
        String file = "D:/log.txt";
        OutputStream outputStream = FileUtil.getOutputStream(file);
        InputStream inputStream = null;
        KubernetesClient client = K8sUtil.createKClient();
        try (LogWatch watch = client.pods().inNamespace("demo").withName("spring-boot-demo-56f6dfdffd-s2d6f").watchLog(outputStream)) {
            inputStream = watch.getOutput();
            //watch.wait();
            TimeUnit.SECONDS.sleep(200L);
            IoUtil.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(outputStream);
            IoUtil.close(inputStream);
        }
    }

    @Test
    public void t10() {

    }
}
