package vip.gpg123.wallpaper.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.wallpaper.domain.StaticWallpaperExtension;

/**
 * @author gaopuguang
 * @description 针对表【cloud_wallpaper】的数据库操作Mapper
 * @createDate 2025-11-24 20:12:31
 * @Entity vip.gpg123.wallpaper.domain.StaticWallpaper
 */
@Mapper
public interface StaticWallpaperMapper extends BaseMapper<StaticWallpaper> {

    /**
     * 分页查询
     *
     * @param page    page
     * @param wrapper w
     * @return r
     */
    IPage<StaticWallpaperExtension> iPage(IPage<StaticWallpaper> page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

    /**
     * 自定义查询
     *
     * @param page            page
     * @param staticWallpaper w
     * @return r
     */
    IPage<StaticWallpaperExtension> page(@Param("page") IPage<StaticWallpaper> page, @Param("qw") StaticWallpaper staticWallpaper);

}




