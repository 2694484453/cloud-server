import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import cn.hutool.system.SystemUtil;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import vip.gpg123.common.utils.helm.HelmApp;
import vip.gpg123.common.utils.helm.HelmUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HelmTest {

    private static final String HELM_REPO_URL = "https://charts.bitnami.com/bitnami";

    private static final String HELM_REPO_NAME = "bitnami";

    private static final String context = "ark.gpg123.vip";

    @Test
    public void test() {
        JSONArray jsonArray = HelmUtils.listJsonArray("", context);
        List<HelmApp> helmApps = HelmUtils.list("", context);
    }

    @Test
    public void test1() {
        String dir = SystemUtil.getUserInfo().getHomeDir();
        System.out.println(dir);
    }

    @Test
    public void install() {
        String path = "/Volumes/gaopuguang/project/helm-repo/whoami-demo/values.yaml";
        String str = FileUtil.readString(path, StandardCharsets.UTF_8);
        Map map = YamlUtil.load(IoUtil.toStream(str, StandardCharsets.UTF_8), Map.class);
        String jsonStr = JSONUtil.toJsonStr(map);
        String[] init = new String[]{"helm", "install"};
        init = ArrayUtil.append(init, "--set-json", jsonStr, "demo", "oss/whoami-demo", "--namespace", "default", "--kube-context", "hcs.gpg123.vip");
        System.out.println(StrUtil.join(" ",init));
        String res = RuntimeUtil.execForStr(StandardCharsets.UTF_8, init);
        System.out.println(res);
    }
}
