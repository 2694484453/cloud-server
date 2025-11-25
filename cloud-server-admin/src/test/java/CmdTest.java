import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CmdTest {

    private static final String host = "hongkong.gpg123.vip";

    private static final Integer port = 22;

    private static final String username = "root";

    private static final String password = "1999y4m30D";

    public Session session() {
        return JschUtil.createSession(host, port, username, password);
    }

    @Test
    public void t1() {
        Session session = session();
        try {
            session.connect();
            String[] init = new String[]{};
            init = ArrayUtil.append(init, "wget");
            String url = "https://dw.uptodown.net/dwn/r5Ybgbgqea5omyGnBn8G4H5kCLojRtPNi3AeU_8fGoXM3C-NhLiTg6tjM-MSTowWtKZOLuJKVr3FF1hdWOd1V5_lVAsb5JJA4UOaDFdQM-jUuBeRuaaIwSb5oiUFOdqz/Bx2kL8bUsN_tZTsW9Hm1MXyHfyYsBS1b5XLeNzGnSUxrc77P37lGKV3np9auscN0uZtlDzRbB7eZUWrBZmYugsEn7LStCZFchefHGLxWaJSh9lIf-ce1CgR6gKFgd00i/5CD6lEDOZ2w4NGXq_vACvB_ThaZS8A8IZaTl--8kEqSdaa0Gmm7uNuNeUvT7zmtlM64c4Te8f7ln-vbIzfgCag==/x-twitter-11-42-0-release-0.apk";
            String path = "/home";
            init = ArrayUtil.append(init, "-P", path, url);
            String cmd = StrUtil.join(" ", (Object) init);
            String res = JschUtil.exec(session, cmd, StandardCharsets.UTF_8);
            System.out.println(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.disconnect();
        }
    }
}
