import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
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

    private static final String[] dirs = new String[]{"2d", "3d", "ai", "widescreen", "phone", "other"};

    @Test
    public void saveOrUpdateStaticWallpaper() {
        List<File> files = new ArrayList<>();
        for (String dir : dirs) {
            List<File> list = FileUtil.loopFiles(sourcePath + dir).stream().filter(file -> (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) && !file.getName().startsWith(".")).collect(Collectors.toList());
            files.addAll(list);
        }
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
            String dirName = dirPath.substring(0, dirPath.indexOf("/"));
            String parentName = f.getParent().substring(f.getParent().lastIndexOf("/") + 1);
            String targetUrl = ossProperties.getDomain() + "/wallpaper/" + URLUtil.encode(dirPath);
            //
            StaticWallpaper staticWallpaper = new StaticWallpaper();

            // 如果不存在
            if (!wallpaperMap.containsKey(dirPath)) {
                // 设置tags
                String[] tags2 = new String[]{};
                tags2 = ArrayUtil.append(tags2, "静态壁纸");
                tags2 = ArrayUtil.append(tags2, f.getName().split(" "));
                tags2 = ArrayUtil.append(tags2, f.getName().split("_"));
                tags2 = ArrayUtil.append(tags2, f.getName().split("-"));

                Map<String, Object> map = getPicResolution(f.getAbsolutePath());
                if (map != null) {
                    Integer width = ObjectUtil.isEmpty(map.get("width")) ? null : (Integer) map.get("width");
                    Integer height = ObjectUtil.isEmpty(map.get("height")) ? null : (Integer) map.get("height");
                    staticWallpaper.setWidth(width);
                    staticWallpaper.setHeight(height);
                }
                staticWallpaper.setCreateTime(DateUtil.date());
                staticWallpaper.setDirName(dirName);
                staticWallpaper.setDirPath(dirPath);
                staticWallpaper.setParentName(parentName);
                staticWallpaper.setName(f.getName());
                staticWallpaper.setUrl(targetUrl);
                staticWallpaper.setTags(StrUtil.join(",", (Object) tags2));
                staticWallpaper.setSize(DataSizeUtil.format(f.length()));
                staticWallpaperService.save(staticWallpaper);
            } else {
                //
                staticWallpaper = wallpaperMap.get(dirPath);
                // url信息
                if (StrUtil.isNotBlank(staticWallpaper.getUrl()) && !staticWallpaper.getUrl().equals(targetUrl)) {
                    staticWallpaper.setUrl(targetUrl);
                    staticWallpaperService.updateById(staticWallpaper);
                }
                // 文件大小信息
                if (StrUtil.isBlank(staticWallpaper.getSize()) || StrUtil.isBlank(staticWallpaper.getSize())) {
                    staticWallpaper.setSize(DataSizeUtil.format(f.length()));
                    staticWallpaperService.updateById(staticWallpaper);
                }
                // dirName
                if (StrUtil.isBlank(staticWallpaper.getDirName()) || !dirName.equals(staticWallpaper.getDirName())) {
                    staticWallpaper.setDirName(dirName);
                    staticWallpaperService.updateById(staticWallpaper);
                }
            }
        });
    }

    @Test
    public void saveOrUpdateDynamicWallpaper() {
        List<File> files = new ArrayList<>();
        List<File> dynamics = FileUtil.loopFiles(sourcePath + "dynamic").stream().filter(file -> file.getName().endsWith(".mp4") && !file.getName().startsWith(".")).collect(Collectors.toList());
        List<File> dynamicPhones = FileUtil.loopFiles(sourcePath + "dynamic_phone").stream().filter(file -> file.getName().endsWith(".mp4") && !file.getName().startsWith(".")).collect(Collectors.toList());
        files.addAll(dynamics);
        files.addAll(dynamicPhones);
        //
        List<DynamicWallpaper> dynamicWallpapers = dynamicWallpaperService.list();
        Map<String, DynamicWallpaper> dynamicWallpaperMap = dynamicWallpapers.stream().collect(Collectors.toMap(DynamicWallpaper::getDirPath, w -> w));
        files.forEach(f -> {
            String dirPath = f.getAbsolutePath().replace(sourcePath, "");
            String dirName = dirPath.substring(0, dirPath.indexOf("/"));
            // 不包含
            if (!dynamicWallpaperMap.containsKey(dirPath)) {
                // 新增
                DynamicWallpaper dynamicWallpaper = new DynamicWallpaper();
                dynamicWallpaper.setName(f.getName());
                dynamicWallpaper.setSize(DataSizeUtil.format(f.length()));
                dynamicWallpaper.setCreateTime(DateUtil.date());
                dynamicWallpaper.setDirPath(f.getAbsolutePath().replace(sourcePath, ""));
                dynamicWallpaper.setUrl(ossProperties.getEndpoint() + "/wallpaper/" + dynamicWallpaper.getDirPath());
                dynamicWallpaperService.save(dynamicWallpaper);
            } else {
                DynamicWallpaper dynamicWallpaper = dynamicWallpaperMap.get(dirPath);
                // 包含，检查url是否正确
                String targetUrl = ossProperties.getEndpoint() + "/wallpaper/" + dynamicWallpaper.getDirPath();
                if (StrUtil.isNotBlank(dynamicWallpaper.getUrl()) && !dynamicWallpaper.getUrl().equals(targetUrl)) {
                    dynamicWallpaper.setUrl(targetUrl);
                    dynamicWallpaperService.updateById(dynamicWallpaper);
                }
                // 检查文件夹名
                if (StrUtil.isBlank(dynamicWallpaper.getDirName()) || !dirName.equals(dynamicWallpaper.getDirName())) {
                    dynamicWallpaper.setDirName(dirName);
                    dynamicWallpaperService.updateById(dynamicWallpaper);
                }
            }
        });
        dynamicWallpapers.forEach(dynamicWallpaper -> {
            String filePath = sourcePath + dynamicWallpaper.getDirPath();
            if (!FileUtil.exist(filePath)) {
                // 不存在就删除
                dynamicWallpaperService.removeById(dynamicWallpaper.getId());
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
        //File file = new File(keywordFilePath);
        //System.out.println(file.getParent().substring(file.getParent().lastIndexOf("/") + 1));
        File file = new File(sourcePath + "dynamic/芙宁娜/芙宁娜001 湖中婷影 吸血姬の日常.mp4");
        String str = file.getAbsolutePath().replace(sourcePath, "");
        System.out.println(str.substring(0, str.indexOf("/")));
    }

    @Test
    public void page() {
        Page<StaticWallpaper> page = new Page<>(1, 10);
        page.addOrder(new OrderItem("create_time", true));
        StaticWallpaper staticWallpaper = new StaticWallpaper();
        staticWallpaper.setName("芙宁娜");
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