import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.wallpaper.domain.Wallpaper;
import vip.gpg123.wallpaper.service.WallpaperService;

import java.io.File;
import java.util.List;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class WallpaperTest {

    @Autowired
    private WallpaperService wallpaperService;

    private static final String sourcePath = "/Volumes/gaopuguang/wallpaper/";

    private static final String domain = "https://aliyunpan.gpg123.vip";

    @Test
    public void test() {
        List<File> files = FileUtil.loopFiles(sourcePath);
        // 遍历文件
        for (File file : files)
            if (file.isFile() && !file.getName().startsWith(".")) {
                String source = "system";
                String type = FileUtil.getType(file);
                Wallpaper wallpaper = new Wallpaper();
                boolean flag = false;
                switch (type) {
                    case "mp4":
                        wallpaper.setType("dynamic");
                        String[] tags1 = new String[]{};
                        tags1 = ArrayUtil.append(tags1, "动态壁纸", "动态", "壁纸");
                        tags1 = ArrayUtil.append(tags1, file.getName().split(" "));
                        tags1 = ArrayUtil.append(tags1, file.getName().split("_"));
                        tags1 = ArrayUtil.append(tags1, file.getName().split("-"));
                        wallpaper.setTags(StrUtil.join(",", (Object) tags1));
                        flag = true;
                        break;
                    case "png":
                    case "jpg":
                        wallpaper.setType("static");
                        String[] tags2 = new String[]{};
                        tags2 = ArrayUtil.append(tags2, "静态壁纸", "静态", "壁纸");
                        tags2 = ArrayUtil.append(tags2, file.getName().split(" "));
                        tags2 = ArrayUtil.append(tags2, file.getName().split("_"));
                        tags2 = ArrayUtil.append(tags2, file.getName().split("-"));
                        wallpaper.setTags(StrUtil.join(",", (Object) tags2));
                        flag = true;
                        break;
                    default:
                        log.info("不是图像文件，忽略。。。");
                        break;
                }
                if (flag) {
                    String localFileName = file.getName();
                    String localFilePath = file.getAbsolutePath();
                    wallpaper.setName(localFileName);
                    wallpaper.setUrl(domain + "/wallpaper/" + URLUtil.encode(localFilePath.replaceAll(sourcePath, "")));

                    // 新增文件
                    wallpaper.setCreateBy("1");
                    wallpaper.setCreateTime(DateUtil.date());
                    wallpaper.setSize(DataSizeUtil.format(FileUtil.size(file)));
                    wallpaperService.save(wallpaper);
                }
            }
    }

    @Test
    public void test2() {
        List<Wallpaper> list = wallpaperService.list();
        for (Wallpaper wallpaper : list) {
            wallpaperService.save(wallpaper);
        }
    }
}
