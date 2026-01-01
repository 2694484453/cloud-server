package vip.gpg123.wallpaper.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.wallpaper.domain.Wallpaper;
import vip.gpg123.wallpaper.service.WallpaperService;
import vip.gpg123.wallpaper.mapper.WallpaperMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_wallpaper】的数据库操作Service实现
* @createDate 2025-11-24 20:12:31
*/
@Service
public class WallpaperServiceImpl extends ServiceImpl<WallpaperMapper, Wallpaper> implements WallpaperService {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    public boolean save(Wallpaper entity) {
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
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体包装类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    public boolean remove(Wrapper<Wallpaper> queryWrapper) {
        return super.remove(queryWrapper);
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    public boolean updateById(Wallpaper entity) {
        return super.updateById(entity);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    @Override
    @DS("wallpaper")
    public List<Wallpaper> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    public List<Wallpaper> list(Wrapper<Wallpaper> queryWrapper) {
        return super.list(queryWrapper);
    }

    /**
     * 查询所有
     */
    @Override
    @DS("wallpaper")
    public List<Wallpaper> list() {
        return super.list();
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    public int count(Wrapper<Wallpaper> queryWrapper) {
        return super.count(queryWrapper);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    public <E extends IPage<Wallpaper>> E page(E page, Wrapper<Wallpaper> queryWrapper) {
        return super.page(page, queryWrapper);
    }
}




