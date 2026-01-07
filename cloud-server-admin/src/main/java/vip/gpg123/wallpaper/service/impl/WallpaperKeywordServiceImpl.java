package vip.gpg123.wallpaper.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.wallpaper.domain.WallpaperKeyword;
import vip.gpg123.wallpaper.service.WallpaperKeywordService;
import vip.gpg123.wallpaper.mapper.WallpaperKeywordMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* @author gaopuguang
* @description 针对表【walpaper_keyword】的数据库操作Service实现
* @createDate 2026-01-07 17:21:22
*/
@Service
public class WallpaperKeywordServiceImpl extends ServiceImpl<WallpaperKeywordMapper, WallpaperKeyword> implements WallpaperKeywordService {

    @Autowired
    private WallpaperKeywordMapper wallpaperKeywordMapper;

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    public boolean save(WallpaperKeyword entity) {
        return super.save(entity);
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */
    @Override
    @DS("wallpaper")
    public boolean saveBatch(Collection<WallpaperKeyword> entityList) {
        return super.saveBatch(entityList);
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Override
    @DS("wallpaper")
    public boolean saveOrUpdateBatch(Collection<WallpaperKeyword> entityList) {
        return super.saveOrUpdateBatch(entityList);
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
    public boolean updateById(WallpaperKeyword entity) {
        return super.updateById(entity);
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    @Override
    @DS("wallpaper")
    public WallpaperKeyword getById(Serializable id) {
        return super.getById(id);
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
    public int count(Wrapper<WallpaperKeyword> queryWrapper) {
        return super.count(queryWrapper);
    }

    /**
     * 随机取数量
     *
     * @param count count
     * @return r
     */
    @Override
    @DS("wallpaper")
    public List<WallpaperKeyword> randomList(Integer count) {
        return wallpaperKeywordMapper.randomList(count);
    }

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    @Override
    @DS("wallpaper")
    public List<WallpaperKeyword> list() {
        return super.list();
    }
}




