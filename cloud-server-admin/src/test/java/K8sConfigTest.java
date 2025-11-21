import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import io.fabric8.kubernetes.api.model.Config;
import org.junit.jupiter.api.Test;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.kubernetes.domain.KubernetesFileConfig;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;


public class K8sConfigTest {

    private static final String configFilePath = K8sUtil.defaultConfigFilePath();

    @Test
    public void test() {
        try {
            String str = FileUtil.readUtf8String(configFilePath);
            JSONObject jsonObject = YamlUtil.load(IoUtil.toStream(str,StandardCharsets.UTF_8), JSONObject.class);

            KubernetesFileConfig fileConfig = BeanUtil.toBean(jsonObject, KubernetesFileConfig.class);
                    //JSONUtil.toBean(jsonObject, KubernetesFileConfig.class);
                    //YamlUtil.load(IoUtil.toStream(str, StandardCharsets.UTF_8), KubernetesFileConfig.class);

            System.out.println(fileConfig);
        } catch (IORuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
