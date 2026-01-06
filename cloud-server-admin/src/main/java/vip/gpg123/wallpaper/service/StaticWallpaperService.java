package vip.gpg123.wallpaper.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
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
     * @param wrapper w
     * @return r
     */
    IPage<StaticWallpaperExtension> iPage(IPage<StaticWallpaper> page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

}
