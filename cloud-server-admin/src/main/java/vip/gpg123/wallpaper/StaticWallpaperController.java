package vip.gpg123.wallpaper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import vip.gpg123.common.core.domain.entity.SysDictData;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.DictUtils;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.framework.config.UmamiConfig;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.service.ISysNoticeService;
import vip.gpg123.umami.domain.WebsiteEvent;
import vip.gpg123.umami.service.WebsiteEventService;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import vip.gpg123.wallpaper.domain.StaticWallpaperQuery;
import vip.gpg123.wallpaper.mapper.StaticWallpaperMapper;
import vip.gpg123.wallpaper.service.StaticWallpaperService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/wallpaper")
@Slf4j
public class StaticWallpaperController extends BaseController {

    @Autowired
    private StaticWallpaperService staticWallpaperService;

    @Autowired
    private StaticWallpaperMapper staticWallpaperMapper;

    @Autowired
    private ISysNoticeService sysNoticeService;

    @Autowired
    private WebsiteEventService websiteEventService;

    @Autowired
    private UmamiConfig.umamiWallpaperProperties umamiWallpaperProperties;

    private static final String defaultType = getDefaultType();

    /**
     * 分类
     *
     * @return r
     */
    @GetMapping("/category")
    @ApiOperation(value = "cate")
    public AjaxResult cate() {
        List<SysDictData> list = DictUtils.getDictCache("wallpaper_category");
        return AjaxResult.success(list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(StaticWallpaper staticWallpaper) {
        List<StaticWallpaper> list = staticWallpaperService.list(new LambdaQueryWrapper<StaticWallpaper>()
                .eq(StaticWallpaper::getCreateBy, getUsername())
                .like(StrUtil.isNotBlank(staticWallpaper.getName()), StaticWallpaper::getName, staticWallpaper.getName())
                .eq(StaticWallpaper::getDirName, ObjectUtil.defaultIfBlank(staticWallpaper.getDirName(), defaultType))
                .orderByDesc(StaticWallpaper::getCreateTime)
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
    public TableDataInfo page(StaticWallpaper staticWallpaper) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<StaticWallpaper> page = staticWallpaperService.page(new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()), new LambdaQueryWrapper<StaticWallpaper>()
                .like(StrUtil.isNotBlank(staticWallpaper.getName()), StaticWallpaper::getName, staticWallpaper.getName())
                .eq(StaticWallpaper::getDirName, ObjectUtil.defaultIfBlank(staticWallpaper.getDirName(), defaultType))
        );
        IPage<StaticWallpaperQuery> wallpaperQueryIPage = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());
        List<StaticWallpaperQuery> list = new ArrayList<>();
        page.getRecords().forEach(item -> {
            StaticWallpaperQuery wallpaperQuery = new StaticWallpaperQuery();
            long visitCount = websiteEventService.count(new LambdaQueryWrapper<WebsiteEvent>()
                    .eq(WebsiteEvent::getWebsiteId, umamiWallpaperProperties.getWebsiteId())
                    .eq(WebsiteEvent::getUrlPath, "/info")
                    .eq(WebsiteEvent::getUrlQuery, "id=" + item.getId())
            );
            long downloadCount = websiteEventService.count(new LambdaQueryWrapper<WebsiteEvent>()
                    .eq(WebsiteEvent::getWebsiteId, umamiWallpaperProperties.getWebsiteId())
                    .eq(WebsiteEvent::getUrlPath, "/download")
                    .eq(WebsiteEvent::getUrlQuery, "id=" + item.getId())
            );
            BeanUtils.copyProperties(item, wallpaperQuery);
            wallpaperQuery.setVisitCount(Math.toIntExact(visitCount));
            wallpaperQuery.setDownloadCount(Math.toIntExact(downloadCount));
            list.add(wallpaperQuery);
        });
        wallpaperQueryIPage.setRecords(list);
        wallpaperQueryIPage.setTotal(page.getTotal());
        return PageUtils.toPageByIPage(wallpaperQueryIPage);
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
        StaticWallpaper staticWallpaper = staticWallpaperService.getById(id);
        StaticWallpaperQuery wallpaperQuery = new StaticWallpaperQuery();
        BeanUtils.copyProperties(staticWallpaper, wallpaperQuery);
        long visitCount = websiteEventService.count(new LambdaQueryWrapper<WebsiteEvent>()
                .eq(WebsiteEvent::getWebsiteId, umamiWallpaperProperties.getWebsiteId())
                .eq(WebsiteEvent::getUrlPath, "/info")
                .eq(WebsiteEvent::getUrlQuery, "id=" + id)
        );
        long downloadCount = websiteEventService.count(new LambdaQueryWrapper<WebsiteEvent>()
                .eq(WebsiteEvent::getWebsiteId, umamiWallpaperProperties.getWebsiteId())
                .eq(WebsiteEvent::getUrlPath, "/download")
                .eq(WebsiteEvent::getUrlQuery, "id=" + id)
        );
        wallpaperQuery.setVisitCount(Math.toIntExact(visitCount + 1));
        wallpaperQuery.setDownloadCount(Math.toIntExact(downloadCount));
        return AjaxResult.success(wallpaperQuery);
    }

    /**
     * 新增
     *
     * @param staticWallpaper w
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody StaticWallpaper staticWallpaper) {
        staticWallpaper.setCreateBy(String.valueOf(getUserId()));
        staticWallpaper.setCreateTime(DateUtil.date());
        // 对密码特殊处理
        boolean save = staticWallpaperService.save(staticWallpaper);
        return save ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     *
     * @param staticWallpaper w
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody StaticWallpaper staticWallpaper) {
        staticWallpaper.setUpdateBy(String.valueOf(getUserId()));
        staticWallpaper.setUpdateTime(DateUtil.date());
        boolean update = staticWallpaperService.updateById(staticWallpaper);
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
        boolean remove = staticWallpaperService.removeById(id);
        if (remove) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * download
     *
     * @param id id
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载")
    public AjaxResult download(@RequestParam(value = "id") String id) {
        System.out.println("下载" + id);
        return AjaxResult.success("此操作仅仅作为记录");
    }

    /**
     * over
     *
     * @return r
     */
    @GetMapping("/overView")
    public AjaxResult overView() {
        Map<String, Object> map = new HashMap<>();
        long total = staticWallpaperService.count();
        map.put("total", total);
        return AjaxResult.success(map);
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

    public static String getDefaultType() {
        return Objects.requireNonNull(DictUtils.getDictCache("wallpaper_category")).get(0).getDictValue();
    }
}
