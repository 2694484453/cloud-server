import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.wallpaper.domain.CloudWallpaper;
import vip.gpg123.wallpaper.service.CloudWallpaperService;

import java.io.File;
import java.util.List;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class OssTest {

    @Autowired
    private OSS ossClient;

    @Autowired
    private CloudWallpaperService cloudWallpaperService;

    @Value("${cloud.aliyun.bucketName}")
    private String bucketName;

    private static final String sourcePath = "/Volumes/gaopuguang/wallpaper/";

    @Value("${cloud.aliyun.ossDomain}")
    private String ossDomain;

    @Test
    public void ListFiles() {
        List<File> files = FileUtil.loopFiles(sourcePath);
        for (File file : files)
            if (file.isFile() && !file.getName().startsWith(".")) {
                String parentPath = file.getParent().replaceAll(sourcePath, "");
                String source = "system";
                String type = FileUtil.getType(file);
                CloudWallpaper cloudWallpaper = new CloudWallpaper();
                boolean flag = false;
                switch (type) {
                    case "mp4":
                        cloudWallpaper.setType("dynamic");
                        String[] tags1 = new String[]{};
                        tags1 = ArrayUtil.append(tags1, "动态壁纸","动态","壁纸");
                        tags1 = ArrayUtil.append(tags1, file.getName().split(" "));
                        tags1 = ArrayUtil.append(tags1, file.getName().split("_"));
                        tags1 = ArrayUtil.append(tags1, file.getName().split("-"));
                        cloudWallpaper.setTags(StrUtil.join(",", (Object) tags1));
                        flag = true;
                        break;
                    case "png":
                    case "jpg":
                        cloudWallpaper.setType("static");
                        String[] tags2 = new String[]{};
                        tags2 = ArrayUtil.append(tags2, "静态壁纸","静态","壁纸");
                        tags2 = ArrayUtil.append(tags2, file.getName().split(" "));
                        tags2 = ArrayUtil.append(tags2, file.getName().split("_"));
                        tags2 = ArrayUtil.append(tags2, file.getName().split("-"));
                        cloudWallpaper.setTags(StrUtil.join(",", (Object) tags2));
                        flag = true;
                        break;
                    default:
                        log.info("不是图像文件，忽略。。。");
                        break;
                }
                if (flag) {
                    // 开始插入
                    log.info("开始插入：{}",file.getName());
                    long count = cloudWallpaperService.count(new LambdaQueryWrapper<CloudWallpaper>()
                            .eq(CloudWallpaper::getSource, source)
                            .eq(CloudWallpaper::getName, file.getName())
                    );
                    // 不存在
                    if (count <= 0) {
                        cloudWallpaper.setSource(source);
                        cloudWallpaper.setCreateBy("1");
                        cloudWallpaper.setCreateTime(DateUtil.date());
                        cloudWallpaper.setSize(DataSizeUtil.format(FileUtil.size(file)));
                        cloudWallpaper.setUrl(ossDomain + "/cloud-wallpaper/" + parentPath + "/" + URLUtil.encode(file.getName()));
                        cloudWallpaper.setName(file.getName());
                        cloudWallpaperService.save(cloudWallpaper);
                        log.info("完成插入：{}",file.getName());
                    } else  {
                        log.info("{}已存在跳过",file.getName());
                    }
                }
                System.out.println(cloudWallpaper);
            }
    }

    @Test
    public void listObjects() throws OSSException {
        ListObjectsV2Result result = ossClient.listObjectsV2(new ListObjectsV2Request(bucketName));
        List<OSSObjectSummary> ossObjectSummaries = result.getObjectSummaries();

        for (OSSObjectSummary s : ossObjectSummaries) {
            System.out.println("\t" + s.getKey());
        }
    }

    @Test
    public void ossTest() {

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
            ListObjectsV2Result result = ossClient.listObjectsV2(listObjectsV2Request);
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
