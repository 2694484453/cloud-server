package vip.gpg123.wallpaper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.gpg123.wallpaper.domain.DynamicWallpaperExtension;

/**
* @author gaopuguang
* @description 针对表【dynamic_wallpaper】的数据库操作Service
* @createDate 2026-01-05 16:08:22
*/
public interface DynamicWallpaperService extends IService<DynamicWallpaper> {

    /**
     * page
     * @param page p
     * @param dynamicWallpaper pw
     * @return r
     */
    IPage<DynamicWallpaperExtension> page(Page<DynamicWallpaper> page, DynamicWallpaper dynamicWallpaper);
}
