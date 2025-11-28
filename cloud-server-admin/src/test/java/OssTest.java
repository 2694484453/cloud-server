import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CopyObjectRequest;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.RenameObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.wallpaper.domain.CloudWallpaper;
import vip.gpg123.wallpaper.service.CloudWallpaperService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes = CloudServerApplication.class)
@Slf4j
public class OssTest {

    @Autowired
    private OSS ossClient;

    @Autowired
    private CloudWallpaperService cloudWallpaperService;

    @Value("${cloud.aliyun.bucketName}")
    private String bucketName;

    private static final String sourcePath = "/Volumes/gaopuguang/wallpaper/";

    private static final String PrefixObject = "cloud-wallpaper/wallpaper";

    @Value("${cloud.aliyun.ossDomain}")
    private String ossDomain;

    @Test
    public void ListFiles() {
        List<File> files = FileUtil.loopFiles(sourcePath);
        List<CloudWallpaper> cloudWallpapers = cloudWallpaperService.list();
        Map<Integer, CloudWallpaper> cloudWallpapersMap = cloudWallpapers.stream().collect(Collectors.toMap(CloudWallpaper::getId, cloudWallpaper -> cloudWallpaper));

        // 遍历文件
        for (File file : files)
            if (file.isFile() && !file.getName().startsWith(".")) {
                String parentDirName = file.getParent().replaceAll(sourcePath, "");
                String source = "system";
                String type = FileUtil.getType(file);
                CloudWallpaper cloudWallpaper = new CloudWallpaper();
                boolean flag = false;
                switch (type) {
                    case "mp4":
                        cloudWallpaper.setType("dynamic");
                        String[] tags1 = new String[]{};
                        tags1 = ArrayUtil.append(tags1, "动态壁纸", "动态", "壁纸");
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
                        tags2 = ArrayUtil.append(tags2, "静态壁纸", "静态", "壁纸");
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
                    String localFileName = file.getName();
                    String localFilePath = file.getAbsolutePath();
                    // 根据hashCode查询是否存在文件
                    cloudWallpaper.setId(file.hashCode());
                    cloudWallpaper.setName(localFileName);
                    cloudWallpaper.setFilePath(file.getAbsolutePath());
                    cloudWallpaper.setUrl(ossDomain + "/" + PrefixObject + "/" + parentDirName + "/" + URLUtil.encode(localFileName));
                    CloudWallpaper searchRes = cloudWallpaperService.getById(cloudWallpaper);

                    // 如果数据库中不包含本地文件id
                    if (!cloudWallpapersMap.containsKey(file.hashCode())) {
                        // 移除数据库记录
                        cloudWallpaperService.removeById(file.hashCode());
                        log.info("{}不存在,移除数据库记录", file.getName());
                    }

                    // 如果存在，判断文件是否移动、是否需要更新
                    if (ObjectUtil.isNotNull(searchRes)) {
                        String filePath = searchRes.getFilePath();

                        // 判断文件名称是否相同
                        if (!searchRes.getName().equals(localFileName)) {
                            // 对oss文件进行重命名
                            RenameObjectRequest renameObjectRequest = new RenameObjectRequest(bucketName, PrefixObject + "/" + parentDirName + "/" + searchRes.getName(), PrefixObject + "/" + parentDirName + "/" + localFileName);
                            ossClient.renameObject(renameObjectRequest);
                            // 更新文件数据库名称
                            cloudWallpaper.setName(localFileName);
                            log.info("{}被修改名称过,同时对数据库进行修改名称", localFileName);
                        } else if (!filePath.equals(localFilePath)) {
                            // 名称相同但是路径不相同，证明本地文件被移动过了
                            cloudWallpaper.setFilePath(localFilePath);
                            String oldPath = cloudWallpaper.getFilePath();
                            String oldParentDirName = oldPath.replaceAll(sourcePath, "").replaceAll("/" + localFilePath, "");
                            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, PrefixObject + "/" + oldParentDirName + "/" + localFileName, bucketName, PrefixObject + "/" + parentDirName + "/" + localFileName);
                            ossClient.copyObject(copyObjectRequest);

                            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
                            deleteObjectsRequest.setKeys(new ArrayList<String>() {{
                                add(PrefixObject + "/" + oldParentDirName + "/" + localFileName);
                            }});
                            // 删除原文件
                            ossClient.deleteObjects(deleteObjectsRequest);
                            log.info("{}被移动过,同时对数据库进行修改路径", localFileName);
                        }
                        cloudWallpaper.setUpdateBy("1");
                        cloudWallpaper.setUpdateTime(DateUtil.date());
                        cloudWallpaperService.updateById(cloudWallpaper);
                    } else {
                        // 新增文件
                        cloudWallpaper.setSource(source);
                        cloudWallpaper.setCreateBy("1");
                        cloudWallpaper.setCreateTime(DateUtil.date());
                        cloudWallpaper.setSize(DataSizeUtil.format(FileUtil.size(file)));
                        cloudWallpaperService.save(cloudWallpaper);
                        // 上传
                        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, PrefixObject + "/" + parentDirName + "/" + localFileName, file);
                        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
                        System.out.println(putObjectResult);
                    }
                }
            }
    }


    @Test
    public void update() {
        List<CloudWallpaper> list = cloudWallpaperService.list();
        list.forEach(cloudWallpaper -> {
            String url = StrUtil.replace(cloudWallpaper.getUrl(), "/cloud-wallpaper/", "/cloud-wallpaper/wallpaper/");
            cloudWallpaper.setUrl(url);
            cloudWallpaperService.updateById(cloudWallpaper);
        });
    }

    @Test
    public void upload() {
        String filePath = "/Volumes/gaopuguang/project/cloud-server/Dockerfile";
        File file = new File(filePath);
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "wallpaper/" + file.getName(), file);
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            System.out.println(putObjectResult.toString());
        } catch (Throwable e) {
            throw new RuntimeException(e);
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
