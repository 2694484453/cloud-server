import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import org.junit.jupiter.api.Test;
import vip.gpg123.common.utils.K8sUtil;

import java.io.IOException;


public class K8sConfigTest {

    private static final String configFilePath = "/Volumes/gaopuguang/project/cloud-server/k8s/config";

    private static final String addPath = "/Volumes/gaopuguang/project/cloud-server/k8s/add";

    private static final String exportPath = "/Volumes/gaopuguang/project/cloud-server/k8s/export";

    @Test
    public void test() {
        String name = "test.gpg123.vip";
        try {
            Config kubeConfig = KubeConfigUtils.parseConfigFromString(FileUtil.readUtf8String(configFilePath));
            Config addConfig = KubeConfigUtils.parseConfigFromString(FileUtil.readUtf8String(addPath));
            kubeConfig.getClusters().addAll(addConfig.getClusters());
            kubeConfig.getContexts().addAll(addConfig.getContexts());
            kubeConfig.getUsers().addAll(addConfig.getUsers());
            K8sUtil.exportToFile(kubeConfig, exportPath);
        } catch (IORuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test1() {
        String name = "test1-job";
        boolean res = K8sUtil.checkResourceName(name);
        System.out.println(res);
    }
}
