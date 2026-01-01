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
import vip.gpg123.wallpaper.domain.Wallpaper;
import vip.gpg123.wallpaper.mapper.WallpaperMapper;
import vip.gpg123.wallpaper.service.WallpaperService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallpaper")
@Slf4j
public class WallpaperController extends BaseController {

    @Autowired
    private WallpaperService wallpaperService;

    @Autowired
    private WallpaperMapper wallpaperMapper;

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
        List<Wallpaper> list = wallpaperService.list(new LambdaQueryWrapper<Wallpaper>()
                .eq(Wallpaper::getCreateBy,  getUsername())
                .like(StrUtil.isNotBlank(name), Wallpaper::getName, name)
                .eq(StrUtil.isNotBlank(type), Wallpaper::getType, type)
                .orderByDesc(Wallpaper::getCreateTime)
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
        IPage<Wallpaper> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        Wallpaper search = new Wallpaper();
        search.setName(name);
        search.setType(type);
        if (ObjectUtil.isNotEmpty(users)) {
            // 只看自己
            search.setCreateBys(users);
        }
        List<Wallpaper> list = wallpaperMapper.page(pageDomain, search);
        page.setRecords(list);
        page.setTotal(wallpaperMapper.list(search).size());
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
        Wallpaper wallpaper = wallpaperService.getById(id);
        return AjaxResult.success(wallpaper);
    }

    /**
     * 新增
     * @param wallpaper w
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody Wallpaper wallpaper) {
        wallpaper.setCreateBy(String.valueOf(getUserId()));
        wallpaper.setCreateTime(DateUtil.date());
        // 对密码特殊处理
        boolean save = wallpaperService.save(wallpaper);
        return save ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     * @param wallpaper w
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody Wallpaper wallpaper) {
        wallpaper.setUpdateBy(String.valueOf(getUserId()));
        wallpaper.setUpdateTime(DateUtil.date());
        boolean update = wallpaperService.updateById(wallpaper);
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
        boolean remove = wallpaperService.removeById(id);
        if (remove) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @GetMapping("/overView")
    public AjaxResult overView() {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("title","壁纸总数");
        map.put("count", wallpaperMapper.selectCount(new LambdaQueryWrapper<Wallpaper>()
        ));
        list.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("title","我的壁纸数");
        map1.put("count", wallpaperMapper.selectCount(new LambdaQueryWrapper<Wallpaper>()
                .eq(Wallpaper::getCreateBy,getUserId())
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
                Wallpaper wallpaper = new Wallpaper();
                boolean flag = false;
                switch (type) {
                    case "mp4":
                        wallpaper.setType("dynamic");
                        String[] tags1 = new String[]{};
                        tags1 = ArrayUtil.append(tags1, "动态壁纸","动态","壁纸");
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
                        tags2 = ArrayUtil.append(tags2, "静态壁纸","静态","壁纸");
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
                    // 开始插入
                    log.info("开始插入：{}",file.getName());
                    long count = wallpaperService.count(new LambdaQueryWrapper<Wallpaper>()
                            .eq(Wallpaper::getName, file.getName())
                    );
                    // 不存在
                    if (count <= 0) {
                        wallpaper.setCreateBy("1");
                        wallpaper.setCreateTime(DateUtil.date());
                        wallpaper.setSize(DataSizeUtil.format(FileUtil.size(file)));
                        wallpaper.setUrl(ossDomain + "/cloud-wallpaper/" + parentPath + "/" + URLUtil.encode(file.getName()));
                        wallpaper.setName(file.getName());
                        wallpaperService.save(wallpaper);
                        log.info("完成插入：{}",file.getName());
                    } else  {
                        log.info("{}已存在跳过",file.getName());
                    }
                }
                System.out.println(wallpaper);
            }
        return AjaxResult.success();
    }
}
