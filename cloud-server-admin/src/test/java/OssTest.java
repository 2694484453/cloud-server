import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OssTest {

    private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAI5tQU6m9mYnspgpVq8Rxu";
    private static String accessKeySecret = "hWWHQ5rLmblqskgowIoHdxBhxiD7Nc";
    private static String bucketName = "dev-gpg";

    @Test
    public void ossTest() {

        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            final String keyPrefix = "wallpaper/";

//            if (!client.doesBucketExist(bucketName)) {
//                client.createBucket(bucketName);
//            }
//
//            // Prepare the environment---inserting 100 test files.
//            List<String> keys = new ArrayList<String>();
//            for (int i = 0; i < 1; i++) {
//                String key = keyPrefix + i;
//                InputStream instream = new ByteArrayInputStream(content.getBytes());
//                client.putObject(bucketName, key, instream);
//                keys.add(key);
//            }
//            System.out.println("Put " + keys.size() + " objects completed.");
            // 列举文件。如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
            // 设定从start-after之后按字母排序开始返回Object。
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName);
            listObjectsV2Request.setStartAfter("wallpaper/");
            listObjectsV2Request.setPrefix("wallpaper/");
            ListObjectsV2Result result = client.listObjectsV2(listObjectsV2Request);
                    //client.listObjectsV2(bucketName, keyPrefix);
            List<OSSObjectSummary> ossObjectSummaries = result.getObjectSummaries();

            for (OSSObjectSummary s : ossObjectSummaries) {
                System.out.println("\t" + s.getKey());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
