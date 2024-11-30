import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ruoyi.git.domain.Gitee;
import org.junit.Test;

/**
 * @author gaopuguang
 * @date 2024/12/1 1:30
 **/
public class HttpTest {

    @Test
    public void t1() {
        Gitee gitee = new Gitee();
        gitee.setApi("https://gitee.com/oauth/token");
        gitee.setClient_id("4205d9756e46effb45c437308d808f7b551414563cbec9f5aa0ec9402e0753d6");
        gitee.setClient_secret("3774068556c18b1e70400229c45530a1385dad8189612dc1dfaf7a5b06a84c04");
        gitee.setGrant_type("authorization_code");
        gitee.setRedirect_uri("https://gpg123.cn/git/gitee");
        String code = "e265f65a97e7bffd29bd237ed9546e3da9cedb402defee3f1d872349015a1696";
        String url = gitee.getApi() + "?client_id=" + gitee.getClient_id() + "&grant_type=" + gitee.getGrant_type() + "&redirect_uri=" + gitee.getRedirect_uri() + "&code=" + code + "&client_secret=" + gitee.getClient_secret();
        System.out.println(url);
        HttpResponse response = HttpUtil.createPost(url)
                .body("{}")
                .execute(false);
        String res = HttpUtil.post(url, "{}");
        System.out.println(res);
        System.out.println(response.body());
    }
}
