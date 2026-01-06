package vip.gpg123.wallpaper.service;

import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_wallpaper_upload】的数据库操作Service
* @createDate 2025-11-29 05:22:05
*/
public interface WallpaperUploadService extends IService<WallpaperUpload> {

    /**
     * one
     * @param wallpaperUpload wu
     * @return r
     */
    WallpaperUpload one(WallpaperUpload wallpaperUpload);

    /**
     * list
     * @param wallpaperUpload wu
     * @return r
     */
    List<WallpaperUpload> list(WallpaperUpload wallpaperUpload);

    /**
     * page
     * @param domain d
     * @param wallpaperUpload wu
     * @return r
     */
    List<WallpaperUpload> page(PageDomain domain, WallpaperUpload wallpaperUpload);
}
