package vip.gpg123.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.setting.yaml.YamlUtil;
import io.fabric8.kubernetes.api.model.Config;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class K8sConfigUtil {

    private static final String configFilePath = K8sUtil.defaultConfigFilePath();

    public static Config getFileConfig() {
        try {
            File configFile = FileUtil.file(configFilePath);
            Config config = YamlUtil.load(FileUtil.getReader(configFile, StandardCharsets.UTF_8), Config.class);
            System.out.println(config);
            return config;
        } catch (IORuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Config config = getFileConfig();
    }
}
