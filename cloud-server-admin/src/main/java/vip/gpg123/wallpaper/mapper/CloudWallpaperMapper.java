package vip.gpg123.wallpaper.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.wallpaper.domain.CloudWallpaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_wallpaper】的数据库操作Mapper
* @createDate 2025-11-24 20:12:31
* @Entity vip.gpg123.wallpaper.domain.CloudWallpaper
*/
@Mapper
public interface CloudWallpaperMapper extends BaseMapper<CloudWallpaper> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    @DS("wallpaper")
    CloudWallpaper one(@Param("qw") CloudWallpaper cloudWallpaper);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    @DS("wallpaper")
    List<CloudWallpaper> list(@Param("qw") CloudWallpaper cloudWallpaper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    @DS("wallpaper")
    List<CloudWallpaper> page(@Param("page") PageDomain page, @Param("qw") CloudWallpaper cloudWallpaper);

}




