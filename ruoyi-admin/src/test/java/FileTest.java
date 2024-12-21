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
        String path = "D:\\project\\my-server\\ruoyi-admin\\src\\main\\resources\\application-common.yml";
        File file = new File(path);
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        System.out.println(mimeType);
    }
}
