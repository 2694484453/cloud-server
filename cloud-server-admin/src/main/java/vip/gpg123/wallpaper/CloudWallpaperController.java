package vip.gpg123.wallpaper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.wallpaper.domain.CloudWallpaper;
import vip.gpg123.wallpaper.mapper.CloudWallpaperMapper;
import vip.gpg123.wallpaper.service.CloudWallpaperService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallpaper")
@Slf4j
public class CloudWallpaperController extends BaseController {

    @Autowired
    private CloudWallpaperService cloudWallpaperService;

    @Autowired
    private CloudWallpaperMapper cloudWallpaperMapper;

    @Autowired
    private OSS ossClient;

    @Value("${cloud.aliyun.ossDomain}")
    private String ossDomain;

    private static final String bucketName = "dev-gpg";

    private static final String keyPrefix = "cloud-wallpaper/";

    private static final String sourcePath = "/Volumes/gaopuguang/wallpaper/";

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name",required = false) String name,
                           @RequestParam(value = "type",required = false) String type) {
        List<CloudWallpaper> list = cloudWallpaperService.list(new LambdaQueryWrapper<CloudWallpaper>()
                .eq(CloudWallpaper::getCreateBy,  getUsername())
                .like(StrUtil.isNotBlank(name), CloudWallpaper::getName, name)
                .eq(StrUtil.isNotBlank(type), CloudWallpaper::getType, type)
                .orderByDesc(CloudWallpaper::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name",required = false) String name,
                              @RequestParam(value = "type",required = false) String type,
                              @RequestParam(value = "source",required = false) String source,
                              @RequestParam(value = "users", required = false) List<String> users) {

        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<CloudWallpaper> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        CloudWallpaper search = new CloudWallpaper();
        search.setName(name);
        search.setType(type);
        if (StrUtil.isNotBlank(source)) {
            // 看来源
            search.setSource(source);
        }
        if (ObjectUtil.isNotEmpty(users)) {
            // 只看自己
            search.setCreateBys(users);
        }
        List<CloudWallpaper> list = cloudWallpaperMapper.page(pageDomain, search);
        page.setRecords(list);
        page.setTotal(cloudWallpaperMapper.list(search).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 详情查询
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam(value = "id",required = false) String id) {
        CloudWallpaper wallpaper = cloudWallpaperService.getById(id);
        return AjaxResult.success(wallpaper);
    }

    /**
     * 新增
     * @param cloudWallpaper w
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody CloudWallpaper cloudWallpaper) {
        cloudWallpaper.setCreateBy(String.valueOf(getUserId()));
        cloudWallpaper.setCreateTime(DateUtil.date());
        // 对密码特殊处理
        boolean save = cloudWallpaperService.save(cloudWallpaper);
        return save ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     * @param cloudWallpaper w
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody CloudWallpaper cloudWallpaper) {
        cloudWallpaper.setUpdateBy(String.valueOf(getUserId()));
        cloudWallpaper.setUpdateTime(DateUtil.date());
        boolean update = cloudWallpaperService.updateById(cloudWallpaper);
        return update ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam(value = "id",required = false) String id) {
        boolean remove = cloudWallpaperService.removeById(id);
        if (remove) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @GetMapping("/overView")
    public AjaxResult overView() {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("title","系统壁纸总数");
        map.put("count",cloudWallpaperMapper.selectCount(new LambdaQueryWrapper<CloudWallpaper>()
                .eq(CloudWallpaper::getSource,"system")
        ));
        list.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("title","我的上传壁纸数");
        map1.put("count",cloudWallpaperMapper.selectCount(new LambdaQueryWrapper<CloudWallpaper>()
                .eq(CloudWallpaper::getSource,"upload")
                .eq(CloudWallpaper::getCreateBy,getUserId())
        ));
        list.add(map1);
        return AjaxResult.success(list);
    }


    @GetMapping("/sync")
    public AjaxResult sync() {
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
        return AjaxResult.success();
    }
}
