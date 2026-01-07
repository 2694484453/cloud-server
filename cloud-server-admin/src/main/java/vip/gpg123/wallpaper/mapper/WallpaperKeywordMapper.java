package vip.gpg123.wallpaper.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.wallpaper.domain.WallpaperKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【walpaper_keyword】的数据库操作Mapper
* @createDate 2026-01-07 17:21:22
* @Entity vip.gpg123.wallpaper.domain.WallpaperKeyword
*/
@Mapper
public interface WallpaperKeywordMapper extends BaseMapper<WallpaperKeyword> {

    /**
     * 随机取数据
     * @param count 数量
     * @return r
     */
    List<WallpaperKeyword> randomList(int count);
}




