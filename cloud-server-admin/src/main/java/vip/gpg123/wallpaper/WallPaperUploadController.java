
package vip.gpg123.wallpaper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import vip.gpg123.wallpaper.service.WallpaperUploadService;

import java.util.List;

@RestController
@RequestMapping("/wallpaper/upload")
public class WallPaperUploadController extends BaseController {

    @Autowired
    private WallpaperUploadService wallpaperUploadService;


    /**
     * 分页查询
     *
     * @param wallpaperUpload wallpaper
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(WallpaperUpload wallpaperUpload) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<WallpaperUpload> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());
        // 查询
        List<WallpaperUpload> wallpaperUploads = wallpaperUploadService.page(pageDomain, wallpaperUpload);
        page.setTotal(wallpaperUploadService.list(wallpaperUpload).size());
        page.setRecords(wallpaperUploads);
        return PageUtils.toPageByIPage(page);
    }

    /**
     * list
     *
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(WallpaperUpload wallpaperUpload) {
        List<WallpaperUpload> wallpaperUploads = wallpaperUploadService.list(wallpaperUpload);
        return AjaxResult.success(wallpaperUploads);
    }
}

