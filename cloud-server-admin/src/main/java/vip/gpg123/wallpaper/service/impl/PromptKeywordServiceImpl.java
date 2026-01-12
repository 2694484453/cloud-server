package vip.gpg123.wallpaper.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.wallpaper.domain.PromptKeyword;
import vip.gpg123.wallpaper.service.PromptKeywordService;
import vip.gpg123.wallpaper.mapper.PromptKeywordMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prompt_keyword】的数据库操作Service实现
* @createDate 2026-01-08 17:00:51
*/
@Service
public class PromptKeywordServiceImpl extends ServiceImpl<PromptKeywordMapper, PromptKeyword> implements PromptKeywordService{

    @Autowired
    private PromptKeywordMapper promptKeywordMapper;

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("wallpaper")
    public boolean save(PromptKeyword entity) {
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
    public boolean updateById(PromptKeyword entity) {
        return super.updateById(entity);
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
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    @Override
    @DS("wallpaper")
    public List<PromptKeyword> list() {
        return super.list();
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    @Override
    @DS("wallpaper")
    public <E extends IPage<PromptKeyword>> E page(E page, Wrapper<PromptKeyword> queryWrapper) {
        return super.page(page, queryWrapper);
    }


    /**
     * page
     *
     * @param page          p
     * @param promptKeyword k
     * @return r
     */
    @Override
    @DS("wallpaper")
    public IPage<PromptKeyword> page(Page<PromptKeyword> page, PromptKeyword promptKeyword) {
        return promptKeywordMapper.page(page,promptKeyword);
    }

    /**
     * list
     *
     * @param promptKeyword key
     * @return r
     */
    @Override
    @DS("wallpaper")
    public List<PromptKeyword> list(PromptKeyword promptKeyword) {
        return promptKeywordMapper.list(promptKeyword);
    }
}




