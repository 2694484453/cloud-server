package vip.gpg123.wallpaper.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_wallpaper_upload】的数据库操作Mapper
* @createDate 2025-11-29 05:22:05
* @Entity vip.gpg123.wallpaper.domain.WallpaperUpload
*/
@Mapper
public interface WallpaperUploadMapper extends BaseMapper<WallpaperUpload> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    WallpaperUpload one(@Param("qw") WallpaperUpload cloudWallpaper);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<WallpaperUpload> list(@Param("qw") WallpaperUpload cloudWallpaper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<WallpaperUpload> page(@Param("page") PageDomain page, @Param("qw") WallpaperUpload cloudWallpaper);

}




