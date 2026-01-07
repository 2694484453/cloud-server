package vip.gpg123.wallpaper.service;

import vip.gpg123.wallpaper.domain.WallpaperKeyword;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【walpaper_keyword】的数据库操作Service
* @createDate 2026-01-07 17:21:22
*/
public interface WallpaperKeywordService extends IService<WallpaperKeyword> {

    /**
     * 随机取数量
     * @param count count
     * @return r
     */
    public List<WallpaperKeyword> randomList(Integer count);
}
