package vip.gpg123.wallpaper.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import vip.gpg123.wallpaper.service.WallpaperUploadService;
import vip.gpg123.wallpaper.mapper.WallpaperUploadMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【cloud_wallpaper_upload】的数据库操作Service实现
 * @createDate 2025-11-29 05:22:05
 */
@Service
public class WallpaperUploadServiceImpl extends ServiceImpl<WallpaperUploadMapper, WallpaperUpload> implements WallpaperUploadService {

    @Autowired
    private WallpaperUploadMapper wallpaperUploadMapper;

    @DS("wallpaper")
    public WallpaperUpload one(WallpaperUpload wallpaperUpload) {
        return wallpaperUploadMapper.one(wallpaperUpload);
    }

    @DS("wallpaper")
    public List<WallpaperUpload> list(WallpaperUpload wallpaperUpload) {
        return wallpaperUploadMapper.list(wallpaperUpload);
    }

    @DS("wallpaper")
    public List<WallpaperUpload> page(PageDomain domain, WallpaperUpload wallpaperUpload) {
        return wallpaperUploadMapper.page(domain, wallpaperUpload);
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    public boolean save(WallpaperUpload entity) {
        return super.save(entity);
    }

    /**
     * 查询总记录数
     *
     * @see Wrappers#emptyWrapper()
     */
    @Override
    @DS("wallpaper")
    public int count() {
        return super.count();
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    public int count(Wrapper<WallpaperUpload> queryWrapper) {
        return super.count(queryWrapper);
    }

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    @Override
    @DS("wallpaper")
    public List<WallpaperUpload> list() {
        return super.list();
    }
}




