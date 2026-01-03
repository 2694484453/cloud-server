package vip.gpg123.wallpaper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
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
import org.springframework.data.redis.core.StringRedisTemplate;
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
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.config.OssConfig;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.service.ISysNoticeService;
import vip.gpg123.wallpaper.domain.Wallpaper;
import vip.gpg123.wallpaper.mapper.WallpaperMapper;
import vip.gpg123.wallpaper.service.WallpaperService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
    private ISysNoticeService sysNoticeService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OssConfig.OssProperties ossProperties;

    @Autowired
    private OSS oss;

    @Value("${cloud.aliyun.ossDomain}")
    private String ossDomain;

    private static final String sourcePath = "/Volumes/gaopuguang/wallpaper/";

    private static final String defaultType = "dongman";

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(Wallpaper wallpaper) {
        List<Wallpaper> list = wallpaperService.list(new LambdaQueryWrapper<Wallpaper>()
                .eq(Wallpaper::getCreateBy, getUsername())
                .like(StrUtil.isNotBlank(wallpaper.getName()), Wallpaper::getName, wallpaper.getType())
                .eq(Wallpaper::getDirName, ObjectUtil.defaultIfBlank(wallpaper.getType(), defaultType))
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
    public TableDataInfo page(Wallpaper wallpaper) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<Wallpaper> page = wallpaperService.page(new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()), new LambdaQueryWrapper<Wallpaper>()
                .like(StrUtil.isNotBlank(wallpaper.getName()), Wallpaper::getName, wallpaper.getName())
                .eq(Wallpaper::getDirName, ObjectUtil.defaultIfBlank(wallpaper.getType(), defaultType))
        );
        // 设置预签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
//        Date expiration = new Date(new Date().getTime() + 60 * 1000L);
//        page.getRecords().forEach(item -> {
//            String objectName = "wallpaper/" + item.getDirPath();
//            URL url = oss.generatePresignedUrl(ossProperties.getBucketName(), objectName, expiration);
//            item.setUrl(url.toString());
//        });
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 详情查询
     *
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam(value = "id", required = false) String id) {
        Wallpaper wallpaper = wallpaperService.getById(id);
        return AjaxResult.success(wallpaper);
    }

    /**
     * 新增
     *
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
     *
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
     *
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam(value = "id", required = false) String id) {
        boolean remove = wallpaperService.removeById(id);
        if (remove) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * download
     *
     * @param id       id
     * @param response response
     * @throws Exception e
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载")
    public AjaxResult download(@RequestParam(value = "id") String id, HttpServletResponse response) throws Exception {
        Wallpaper wallpaper = wallpaperService.getById(id);
        //
        try {
            SysUser sysUser = SecurityUtils.getLoginUser().getUser();
            // 检查是否具有下载权限,生成签名地址
            // 设置预签名URL过期时间，单位为毫秒。本示例以设置过期时间为2分钟为例。
            Date expiration = new Date(new Date().getTime() + 120 * 1000L);
            URL url = oss.generatePresignedUrl(ossProperties.getBucketName(), "wallpaper/" + wallpaper.getDirPath(), expiration);
            return AjaxResult.success("",url.toString());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage() + ",请先登录");
        }
    }

    /**
     * over
     *
     * @return r
     */
    @GetMapping("/overView")
    public AjaxResult overView() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "壁纸总数");
        map.put("count", wallpaperMapper.selectCount(new LambdaQueryWrapper<Wallpaper>()
        ));
        list.add(map);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("title", "我的壁纸数");
        map1.put("count", wallpaperMapper.selectCount(new LambdaQueryWrapper<Wallpaper>()
                .eq(Wallpaper::getCreateBy, getUserId())
        ));
        list.add(map1);
        return AjaxResult.success(list);
    }

    /**
     * 公告
     *
     * @return r
     */
    @GetMapping("/notice")
    public AjaxResult notice() {
        List<SysNotice> list = sysNoticeService.list(new LambdaQueryWrapper<SysNotice>()
                .eq(SysNotice::getNoticeType, "wallpaper")
        );
        return AjaxResult.success(list);
    }

}
