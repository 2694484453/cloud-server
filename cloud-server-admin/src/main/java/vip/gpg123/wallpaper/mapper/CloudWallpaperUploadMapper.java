package vip.gpg123.wallpaper.mapper;

import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.wallpaper.domain.CloudWallpaper;
import vip.gpg123.wallpaper.domain.CloudWallpaperUpload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_wallpaper_upload】的数据库操作Mapper
* @createDate 2025-11-29 05:22:05
* @Entity vip.gpg123.wallpaper.domain.CloudWallpaperUpload
*/
public interface CloudWallpaperUploadMapper extends BaseMapper<CloudWallpaperUpload> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    CloudWallpaperUpload one(@Param("qw") CloudWallpaperUpload cloudWallpaper);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<CloudWallpaperUpload> list(@Param("qw") CloudWallpaperUpload cloudWallpaper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<CloudWallpaperUpload> page(@Param("page") PageDomain page, @Param("qw") CloudWallpaperUpload cloudWallpaper);

}




