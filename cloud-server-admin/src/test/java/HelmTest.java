import cn.hutool.json.JSONArray;
import cn.hutool.system.SystemUtil;
import org.junit.Test;
import vip.gpg123.common.utils.helm.HelmApp;
import vip.gpg123.common.utils.helm.HelmUtils;

import java.util.List;

public class HelmTest {

    private static final String HELM_REPO_URL = "https://charts.bitnami.com/bitnami";

    private static final String HELM_REPO_NAME = "bitnami";

    private static final String context = "ark.gpg123.vip";

    @Test
    public void test() {
        JSONArray jsonArray = HelmUtils.listJsonArray("",context);
        List<HelmApp> helmApps = HelmUtils.list("",context);
    }

    @Test
    public void test1() {
       String dir =  SystemUtil.getUserInfo().getHomeDir();
        System.out.println(dir);
    }
}
