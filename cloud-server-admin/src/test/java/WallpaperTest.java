import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.framework.config.AliYunConfig;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import vip.gpg123.wallpaper.domain.StaticWallpaperExtension;
import vip.gpg123.wallpaper.domain.WallpaperKeyword;
import vip.gpg123.wallpaper.mapper.StaticWallpaperMapper;
import vip.gpg123.wallpaper.service.DynamicWallpaperService;
import vip.gpg123.wallpaper.service.StaticWallpaperService;
import vip.gpg123.wallpaper.service.WallpaperKeywordService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes = CloudServerApplication.class)
@Slf4j
public class WallpaperTest {

    @Autowired
    private StaticWallpaperService staticWallpaperService;

    @Autowired
    private StaticWallpaperMapper staticWallpaperMapper;

    @Autowired
    private DynamicWallpaperService dynamicWallpaperService;

    @Autowired
    private WallpaperKeywordService wallpaperKeywordService;

    @Autowired
    private AliYunConfig.OssProperties ossProperties;

    private static final String sourcePath = "/Volumes/gaopuguang/wallpaper/";

    private static final String keywordFilePath = "/Volumes/gaopuguang/project/cloud-server/cloud-server-admin/src/test/java/keyword.txt";

    private static final String[] dirs = new String[]{};

    @Test
    public void ListFiles() {
        List<File> files = FileUtil.loopFiles(sourcePath).stream().filter(file -> (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) && !file.getName().startsWith(".")).collect(Collectors.toList());
        List<StaticWallpaper> staticWallpapers = staticWallpaperService.list();
        Map<String, StaticWallpaper> wallpaperMap = staticWallpapers.stream().collect(Collectors.toMap(StaticWallpaper::getDirPath, wallpaper -> wallpaper));
        // 判断删除的
        staticWallpapers.forEach(w -> {
            String filePath = sourcePath + w.getDirPath();
            // 找不到文件，则进行删除
            if (!FileUtil.exist(filePath)) {
                staticWallpaperService.removeById(w.getId());
            }
        });
        // 判断新增的
        files.forEach(f -> {
            String dirPath = f.getAbsolutePath().replace(sourcePath, "");
            String dirName = dirPath.replace("/" + f.getName(), "");
            String parentName = f.getParent().substring(f.getParent().lastIndexOf("/") + 1);
            String targetUrl = ossProperties.getEndpoint() + "/wallpaper/" + URLUtil.encode(dirPath);
            // 如果不存在
            if (!wallpaperMap.containsKey(dirPath)) {
                // 设置tags
                String[] tags2 = new String[]{};
                tags2 = ArrayUtil.append(tags2, "静态壁纸");
                tags2 = ArrayUtil.append(tags2, f.getName().split(" "));
                tags2 = ArrayUtil.append(tags2, f.getName().split("_"));
                tags2 = ArrayUtil.append(tags2, f.getName().split("-"));

                StaticWallpaper staticWallpaper = new StaticWallpaper();
                Map<String, Object> map = getPicResolution(f.getAbsolutePath());
                if (map != null) {
                    Integer width = ObjectUtil.isEmpty(map.get("width")) ? null : (Integer) map.get("width");
                    Integer height = ObjectUtil.isEmpty(map.get("height")) ? null : (Integer) map.get("height");
                    staticWallpaper.setWidth(width);
                    staticWallpaper.setHeight(height);
                }
                staticWallpaper.setCreateTime(DateUtil.date());
                staticWallpaper.setDirName(dirName);
                staticWallpaper.setDirPath(f.getAbsolutePath());
                staticWallpaper.setParentName(parentName);
                staticWallpaper.setName(f.getName());
                staticWallpaper.setUrl(targetUrl);
                staticWallpaper.setTags(StrUtil.join(",", (Object) tags2));
                staticWallpaperService.save(staticWallpaper);
            } else {
                //
                StaticWallpaper staticWallpaper = wallpaperMap.get(dirPath);
                // 比较其他信息是否正确
                if (StrUtil.isNotBlank(staticWallpaper.getUrl()) && !staticWallpaper.getUrl().equals(targetUrl)) {
                    staticWallpaper.setUrl(targetUrl);
                    staticWallpaperService.updateById(staticWallpaper);
                }
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
                Map<String, Object> map = getPicResolution(filePath);
                Integer width = ObjectUtil.isEmpty(map.get("width")) ? null : (Integer) map.get("width");
                Integer height = ObjectUtil.isEmpty(map.get("height")) ? null : (Integer) map.get("height");
                w.setWidth(width);
                w.setHeight(height);
                staticWallpaperService.updateById(w);
                System.out.println("图片名称：" + w.getName() + ",图片尺寸: " + width + " x " + height + " 像素");
            }
            if (exists) {
                File file = new File(filePath);
                String relativePath = file.getAbsolutePath().replace(sourcePath, "").replace("/" + file.getName(), "");
                w.setDirName(relativePath);
                staticWallpaperService.updateById(w);
                System.out.println("更新" + w.getId());
            }
        });
    }

    @Test
    public void saveOrUpdateDynamicWallpaper() {
        List<File> files = FileUtil.loopFiles(sourcePath + "dynamic").stream().filter(file -> file.getName().endsWith(".mp4") && !file.getName().startsWith(".")).collect(Collectors.toList());
        Map<String, File> fileMap = files.stream().collect(Collectors.toMap(File::getName, file -> file));
        //
        List<DynamicWallpaper> dynamicWallpapers = dynamicWallpaperService.list();
        Map<String, DynamicWallpaper> dynamicWallpaperMap = dynamicWallpapers.stream().collect(Collectors.toMap(DynamicWallpaper::getName, w -> w));
        files.forEach(f -> {
            // 不包含
            if (!dynamicWallpaperMap.containsKey(f.getName())) {
                // 新增
                DynamicWallpaper dynamicWallpaper = new DynamicWallpaper();
                dynamicWallpaper.setName(f.getName());
                dynamicWallpaper.setSize(DataSizeUtil.format(f.length()));
                dynamicWallpaper.setCreateTime(DateUtil.date());
                dynamicWallpaper.setDirPath(f.getAbsolutePath().replace(sourcePath, ""));
                dynamicWallpaper.setUrl(ossProperties.getEndpoint() + "/wallpaper/" + dynamicWallpaper.getDirPath());
                dynamicWallpaperService.save(dynamicWallpaper);
            }
        });
        dynamicWallpapers.forEach(dynamicWallpaper -> {
            if (!fileMap.containsKey(dynamicWallpaper.getName())) {
                // 不存在就删除
                dynamicWallpaperService.removeById(dynamicWallpaper.getId());
            } else {
                // 包含，检查url是否正确
                String targetUrl = ossProperties.getEndpoint() + "/wallpaper/" + dynamicWallpaper.getDirPath();
                if (!dynamicWallpaper.getUrl().equals(targetUrl)) {
                    dynamicWallpaper.setUrl(targetUrl);
                    dynamicWallpaperService.updateById(dynamicWallpaper);
                }
            }
        });
    }

    @Test
    public void saveOrUpdateKeyWord() {
        List<WallpaperKeyword> wallpaperKeywords = wallpaperKeywordService.list();
        Map<String, WallpaperKeyword> map = wallpaperKeywords.stream().collect(Collectors.toMap(WallpaperKeyword::getKeywordName, w -> w));
        //
        List<String> keywords = FileUtil.readLines(keywordFilePath, StandardCharsets.UTF_8);
        keywords.forEach(keyword -> {
            if (!map.containsKey(keyword)) {
                // 不存在
                // 生成三个0-255之间的随机数（红、绿、蓝）
                int red = RandomUtil.randomInt(0, 256);
                int green = RandomUtil.randomInt(0, 256);
                int blue = RandomUtil.randomInt(0, 256);

                // 转换为十六进制字符串（两位，不足补0）
                String color = String.format("#%02X%02X%02X", red, green, blue);
                WallpaperKeyword wallpaperKeyword = new WallpaperKeyword();
                wallpaperKeyword.setKeywordName(keyword);
                wallpaperKeyword.setCreateTime(DateUtil.date());
                wallpaperKeyword.setKeywordColor(color);
                wallpaperKeywordService.save(wallpaperKeyword);
            }
        });
    }

    @Test
    public void test() {
        File file = new File(keywordFilePath);
        System.out.println(file.getParent().substring(file.getParent().lastIndexOf("/") + 1));
    }

    @Test
    public void page() {
        IPage<StaticWallpaper> page = new Page<>(1, 10);
        StaticWallpaper staticWallpaper = new StaticWallpaper();
        staticWallpaper.setName("test");
        IPage<StaticWallpaperExtension> pageRes = staticWallpaperMapper.page(page, staticWallpaper);
        System.out.println(pageRes);
    }

    public Map<String, Object> getPicResolution(String filePath) {
        Map<String, Object> map = new HashMap<>();
        // 获取尺寸
        System.out.println("获取" + FileUtil.getName(filePath));
        // 2. 将图片读取为 BufferedImage 对象
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filePath));
            if (image != null) {
                // 3. 获取宽度和高度
                int width = image.getWidth();
                int height = image.getHeight();
                map.put("width", width);
                map.put("height", height);
                return map;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       return null;
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
                    case "png":
                    case "jpg":

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
                    staticWallpaper.setUrl(ossProperties.getEndpoint() + "/wallpaper/" + URLUtil.encode(relativePath));
                    staticWallpapers.add(staticWallpaper);
                }
            }
        }
        return staticWallpapers;
    }
}
