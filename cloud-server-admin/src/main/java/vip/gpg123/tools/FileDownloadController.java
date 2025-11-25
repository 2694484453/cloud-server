package vip.gpg123.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Session;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileDownloadController {

    private static final String domain = "https://file-share.gpg123.vip";

    private static final String host = "hongkong.gpg123.vip";

    private static final Integer port = 22;

    private static final String username = "root";

    private static final String password = "1999y4m30D";

    private static final String path = "/home/download";

    public Session session() {
        return JschUtil.createSession(host, port, username, password);
    }

    /**
     * 文件下载
     * @param params p
     */
    @PostMapping("/download")
    public AjaxResult download(@RequestBody Map<String, Object> params) {
        String url = params.containsKey("url") ? (String) params.get("url") : "";
        String fileName = FileUtil.getName(url);
        String res = "";
        if (StrUtil.isNotBlank(url)) {
            Session session = session();
            try {
                session.connect();
                String[] init = new String[]{};
                init = ArrayUtil.append(init, "wget");
                init = ArrayUtil.append(init, "-P", path, url);
                String cmd = StrUtil.join(" ", (Object) init);
                res = JschUtil.exec(session, cmd, StandardCharsets.UTF_8);
                System.out.println(res);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                session.disconnect();
            }
        }
        return StrUtil.isNotBlank(res) ? AjaxResult.success(res) : AjaxResult.error("下载失败");
    }
}
