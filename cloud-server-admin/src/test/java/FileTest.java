import org.apache.tika.Tika;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author gaopuguang
 * @date 2024/12/21 17:34
 **/
public class FileTest {

    @Test
    public void t1() throws IOException {
        String path = "D:\\project\\my-server\\cloud-server-admin\\src\\main\\resources\\application-common.yml";
        File file = new File(path);
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        System.out.println(mimeType);
    }

    @Test
    public void t2() throws IOException {
        String url = "https://codeup.aliyun.com/63b19ef08de0fe7a6554f8c1/caddy.git";
        String path = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        System.out.println(path);
    }
}
