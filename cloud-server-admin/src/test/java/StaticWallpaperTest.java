import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import vip.gpg123.wallpaper.service.StaticWallpaperService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes = CloudServerApplication.class)
@Slf4j
public class StaticWallpaperTest {

    @Autowired
    private StaticWallpaperService staticWallpaperService;

    private static final String ossDomain = "https://dev-gpg.oss-cn-hangzhou.aliyuncs.com";

    private static final String sourcePath = "/Volumes/gaopuguang/wallpaper/";

    @Test
    public void ListFiles() {
        List<StaticWallpaper> files = findWallpapers();
        List<StaticWallpaper> staticWallpapers = staticWallpaperService.list();
        Map<String, StaticWallpaper> filsMap = files.stream().collect(Collectors.toMap(StaticWallpaper::getDirPath, wallpaper -> wallpaper));
        Map<String, StaticWallpaper> wallpaperMap = staticWallpapers.stream().collect(Collectors.toMap(StaticWallpaper::getDirPath, wallpaper -> wallpaper));
        // 判断删除的
        staticWallpapers.forEach(w -> {
            if (!filsMap.containsKey(w.getDirPath())) {
                staticWallpaperService.removeById(w.getId());
            }
        });
        // 判断新增的
        files.forEach(f -> {
            if (!wallpaperMap.containsKey(f.getDirPath())) {
                staticWallpaperService.save(f);
            }
        });
    }

    @Test
    public void update() {
        List<StaticWallpaper> staticWallpapers = staticWallpaperService.list();
        staticWallpapers.forEach(w -> {
            String filePath = sourcePath + "/" + w.getDirPath();
            boolean exists = FileUtil.exist(filePath);
            if (exists && (ObjectUtil.isNull(w.getWidth()) || ObjectUtil.isNull(w.getHeight()))) {
                // 获取尺寸
                System.out.println("获取" + w.getName());
                // 2. 将图片读取为 BufferedImage 对象
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new File(filePath));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // 3. 获取宽度和高度
                int width = image.getWidth();
                int height = image.getHeight();
                w.setWidth(width);
                w.setHeight(height);
                staticWallpaperService.updateById(w);
                System.out.println("图片名称：" + w.getName() + ",图片尺寸: " + width + " x " + height + " 像素");
            }
        });
    }

    public List<StaticWallpaper> findWallpapers() {
        List<File> files = FileUtil.loopFiles(sourcePath);
        List<StaticWallpaper> staticWallpapers = new ArrayList<>();
        // 遍历文件
        for (File file : files) {
            if (file.isFile() && !file.getName().startsWith(".")) {
                String relativePath = file.getAbsolutePath().replace(sourcePath, "");
                String type = FileUtil.getType(file);
                StaticWallpaper staticWallpaper = new StaticWallpaper();
                boolean flag = false;
                switch (type) {
                    case "mp4":
                        String[] tags1 = new String[]{};
                        tags1 = ArrayUtil.append(tags1, "动态壁纸", "动态", "壁纸");
                        tags1 = ArrayUtil.append(tags1, file.getName().split(" "));
                        tags1 = ArrayUtil.append(tags1, file.getName().split("_"));
                        tags1 = ArrayUtil.append(tags1, file.getName().split("-"));
                        staticWallpaper.setTags(StrUtil.join(",", (Object) tags1));
                        flag = true;
                        break;
                    case "png":
                    case "jpg":
                        String[] tags2 = new String[]{};
                        tags2 = ArrayUtil.append(tags2, "静态壁纸", "静态", "壁纸");
                        tags2 = ArrayUtil.append(tags2, file.getName().split(" "));
                        tags2 = ArrayUtil.append(tags2, file.getName().split("_"));
                        tags2 = ArrayUtil.append(tags2, file.getName().split("-"));
                        staticWallpaper.setTags(StrUtil.join(",", (Object) tags2));
                        flag = true;
                        break;
                    default:
                        log.info("不是图像文件，忽略。。。");
                        break;
                }
                if (flag) {
                    staticWallpaper.setDirPath(relativePath);
                    staticWallpaper.setCreateTime(DateUtil.date());
                    staticWallpaper.setName(file.getName());
                    staticWallpaper.setSize(DataSizeUtil.format(file.length()));
                    staticWallpaper.setUrl(ossDomain + "/staticWallpaper/" + URLUtil.encode(relativePath));
                    staticWallpapers.add(staticWallpaper);
                }
            }
        }
        return staticWallpapers;
    }
}
