package vip.gpg123.wallpaper.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;
import vip.gpg123.wallpaper.service.DynamicWallpaperService;
import vip.gpg123.wallpaper.mapper.DynamicWallpaperMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
* @author gaopuguang
* @description 针对表【dynamic_wallpaper】的数据库操作Service实现
* @createDate 2026-01-05 16:08:22
*/
@Service
public class DynamicWallpaperServiceImpl extends ServiceImpl<DynamicWallpaperMapper, DynamicWallpaper> implements DynamicWallpaperService{


    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    public boolean save(DynamicWallpaper entity) {
        return super.save(entity);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    @DS("wallpaper")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    public boolean updateById(DynamicWallpaper entity) {
        return super.updateById(entity);
    }
}




