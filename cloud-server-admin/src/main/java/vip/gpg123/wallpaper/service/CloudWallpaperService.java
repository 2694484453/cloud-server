package vip.gpg123.wallpaper.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import vip.gpg123.wallpaper.domain.CloudWallpaper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_wallpaper】的数据库操作Service
* @createDate 2025-11-24 20:12:31
*/
public interface CloudWallpaperService extends IService<CloudWallpaper> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    default boolean save(CloudWallpaper entity) {
        return IService.super.save(entity);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    @DS("wallpaper")
    default boolean removeById(Serializable id) {
        return IService.super.removeById(id);
    }

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体包装类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    default boolean remove(Wrapper<CloudWallpaper> queryWrapper) {
        return IService.super.remove(queryWrapper);
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    default boolean updateById(CloudWallpaper entity) {
        return IService.super.updateById(entity);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    @Override
    @DS("wallpaper")
    default List<CloudWallpaper> listByIds(Collection<? extends Serializable> idList) {
        return IService.super.listByIds(idList);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    default List<CloudWallpaper> list(Wrapper<CloudWallpaper> queryWrapper) {
        return IService.super.list(queryWrapper);
    }

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    @Override
    @DS("wallpaper")
    default List<CloudWallpaper> list() {
        return IService.super.list();
    }
}
