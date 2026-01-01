package vip.gpg123.wallpaper;

import cn.hutool.core.io.unit.DataSizeUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/wallpaper/oss")
public class WallPaperUploadController extends BaseController {

    @Autowired
    private OSS ossClient;

    @Value("${cloud.aliyun.ossDomain}")
    private String ossDomain;

    private static final String bucketName = "dev-gpg";

    private static final String keyPrefix = "wallpaper/";

    /**
     * list
     *
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list() {
        List<HashMap<String,Object>> list = new ArrayList<>();
        try {
            // 列举文件。如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName);
            listObjectsV2Request.setStartAfter(keyPrefix);
            listObjectsV2Request.setPrefix(keyPrefix);
            ListObjectsV2Result result = ossClient.listObjectsV2(listObjectsV2Request);
            List<OSSObjectSummary> ossObjectSummaries = result.getObjectSummaries();
            ossObjectSummaries.forEach(summary -> {
                HashMap<String,Object> map = new HashMap<>();
                map.put("name", summary.getKey().replaceAll(keyPrefix, ""));
                map.put("size", DataSizeUtil.format(summary.getSize()));
                map.put("lastModified", summary.getLastModified());
                map.put("eTag", summary.getETag());
                map.put("url", ossDomain + "/" + summary.getKey());
                list.add(map);
            });
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        }
        return AjaxResult.success(list);
    }
}
