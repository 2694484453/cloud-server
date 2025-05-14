import cn.hutool.setting.Setting;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.junit.Test;
import vip.gpg123.nas.domain.NasFrpClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingTest {

    @Test
    public void test() throws IOException {
        Setting setting = new Setting("/Volumes/gaopuguang/project/docker-compose/nas/frp/client/frpc.toml");
        System.out.println(setting);
        System.out.println(setting.get("auth.token"));
        System.out.println(setting.getSetting("common"));
        new TomlWriter().write(setting, new File("/Volumes/gaopuguang/project/docker-compose/nas/frp/client/frpc3.toml"));
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
        Setting setting1 = setting.setByGroup("proxies","","");
        for (int i = 0; i < 10; i++) {
            setting1.set("name", String.valueOf(i));
            setting1.set("type", "22");
            setting1.set("localIp", "127.0.0.1");
            setting1.set("localPort", String.valueOf(i));
            setting1.set("customDomains", i +".gpg123.vip");
            setting.addSetting(setting1);
        }
        setting.set("healthCheck.type", "http");
        setting.set("healthCheck.path", "/status");
        setting.set("healthCheck.timeoutSeconds", "3");
        setting.set("healthCheck.maxFailed", "3");
        setting.set("healthCheck.intervalSeconds", "10");
        new TomlWriter().write(setting, new File("/Volumes/gaopuguang/project/docker-compose/nas/frp/client/frpc2.toml"));
    }

    @Test
    public void test3() throws IOException {
        Toml setting = new Toml().read(new File("/Volumes/gaopuguang/project/docker-compose/nas/frp/client/frpc3.toml"));
        System.out.println(setting);
    }

    @Test
    public void test4() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("serverAddr = "+"\"hcs.gpg123.vip\""+"\n");
        sb.append("auth.token = "+"123456"+"\n");
        FileWriter fileWriter = new FileWriter("/Volumes/gaopuguang/project/docker-compose/nas/frp/client/frpc4.toml");
        fileWriter.write(sb.toString());
        fileWriter.close();
    }
}
