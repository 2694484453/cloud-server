package vip.gpg123.wallpaper.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.wallpaper.domain.DynamicWallpaperExtension;

/**
* @author gaopuguang
* @description 针对表【dynamic_wallpaper】的数据库操作Mapper
* @createDate 2026-01-05 16:08:22
* @Entity vip.gpg123.wallpaper.domain.DynamicWallpaper
*/
@Mapper
public interface DynamicWallpaperMapper extends BaseMapper<DynamicWallpaper> {

    /**
     * 自定义查询
     *
     * @param page            page
     * @param dynamicWallpaper d
     * @return r
     */
    IPage<DynamicWallpaperExtension> page(Page<DynamicWallpaper> page, @Param("qw") DynamicWallpaper dynamicWallpaper);

}




