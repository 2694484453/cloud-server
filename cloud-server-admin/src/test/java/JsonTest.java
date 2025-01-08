import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import vip.gpg123.build.domain.GiteeRepo;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:46
 **/
public class JsonTest {

    @Test
    public void gitee(){
        String path = "D:\\project\\my-server\\.my-server\\admin\\gitee-repo.json";
        String content = FileUtil.readString(path, StandardCharsets.UTF_8);
        List<GiteeRepo> list = JSONUtil.toList(content, GiteeRepo.class);
        System.out.println(list);
    }
}
