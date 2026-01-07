package vip.gpg123.wallpaper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.gpg123.wallpaper.domain.StaticWallpaperExtension;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_wallpaper】的数据库操作Service
* @createDate 2025-11-24 20:12:31
*/
public interface StaticWallpaperService extends IService<StaticWallpaper> {

    /**
     * 分页查询
     * @param page page
     * @param staticWallpaper s
     * @return r
     */
    IPage<StaticWallpaperExtension> page(Page<StaticWallpaper> page, StaticWallpaper staticWallpaper);

    /**
     * tags
     * @return r
     */
    List<Object> tags();
}
