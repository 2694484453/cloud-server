import cn.hutool.setting.Setting;
import com.moandjiezana.toml.TomlWriter;
import org.junit.Test;
import vip.gpg123.nas.domain.NasFrpClient;

import java.io.File;
import java.io.IOException;

public class SettingTest {

    @Test
    public void test() {
        Setting setting = new Setting("/Volumes/gaopuguang/project/docker-compose/nas/frp/client/frpc.toml");
        System.out.println(setting);
        System.out.println(setting.get("auth.token"));
        System.out.println(setting.getSetting("common"));
    }

    @Test
    public void test2() throws IOException {
        Setting setting = new Setting();
        // 配置服务端
        setting.set("serverAddr", "gpg123.vip");
        setting.set("serverPort", String.valueOf(80));
        setting.setByGroup("auth.token", "common","1");
        // client-admin-ui
        setting.entrySet("webServer");
        setting.set("webServer.addr", "127.0.0.1");
        setting.set("webServer.port", "8081");
        setting.set("webServer.user", "admin");
        setting.set("webServer.password", "admin");
        // proxies
//        for (NasFrpClient nasFrpClient : list) {
//            setting.entrySet("[[proxies]]");
//            setting.set("name", nasFrpClient.getName());
//            setting.set("type", nasFrpClient.getType());
//            setting.set("localIp", nasFrpClient.getLocalIp());
//            setting.set("localPort", nasFrpClient.getLocalPort().toString());
//            setting.set("customDomains", nasFrpClient.getCustomDomains());
//        }
        setting.set("healthCheck.type", "http");
        setting.set("healthCheck.path", "/status");
        setting.set("healthCheck.timeoutSeconds", "3");
        setting.set("healthCheck.maxFailed", "3");
        setting.set("healthCheck.intervalSeconds", "10");
        new TomlWriter().write(setting, new File("/Volumes/gaopuguang/project/docker-compose/nas/frp/client/frpc2.toml"));
    }

}
