import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.framework.config.AliYunConfig;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import vip.gpg123.wallpaper.service.StaticWallpaperService;

import java.io.File;
import java.util.List;

@SpringBootTest(classes = CloudServerApplication.class)
@Slf4j
public class OssTest {

    @Autowired
    private OSS ossClient;

    @Autowired
    private AliYunConfig.OssProperties ossProperties;

    @Autowired
    private StaticWallpaperService staticWallpaperService;

    @Test
    public void update() {
        List<StaticWallpaper> list = staticWallpaperService.list();
        list.forEach(cloudWallpaper -> {
            String dirPath = cloudWallpaper.getDirPath();
            cloudWallpaper.setDirName(dirPath.replaceAll("/" + cloudWallpaper.getName(), ""));
            staticWallpaperService.updateById(cloudWallpaper);
        });
    }

    @Test
    public void upload() {
        String filePath = "/Volumes/gaopuguang/project/cloud-server/Dockerfile";
        File file = new File(filePath);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketName(), "wallpaper/" + file.getName(), file);
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            System.out.println(putObjectResult.toString());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listObjects() throws OSSException {
        ListObjectsV2Result result = ossClient.listObjectsV2(new ListObjectsV2Request(ossProperties.getBucketName()));
        List<OSSObjectSummary> ossObjectSummaries = result.getObjectSummaries();

        for (OSSObjectSummary s : ossObjectSummaries) {
            System.out.println("\t" + s.getKey());
        }
    }

    @Test
    public void ossTest() {
        try {
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
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(ossProperties.getBucketName());
            listObjectsV2Request.setPrefix("wallpaper/");
            listObjectsV2Request.setMaxKeys(1000);
            ListObjectsV2Result result = ossClient.listObjectsV2(listObjectsV2Request);
            //client.listObjectsV2(bucketName, keyPrefix);
            List<OSSObjectSummary> ossObjectSummaries = result.getObjectSummaries();

            for (OSSObjectSummary s : ossObjectSummaries) {
                if (s.getKey().substring(s.getKey().lastIndexOf("/") + 1).startsWith(".")) {
                    System.out.println("\t" + s.getKey());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
