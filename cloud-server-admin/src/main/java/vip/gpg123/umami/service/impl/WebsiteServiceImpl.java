package vip.gpg123.umami.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.umami.domain.Website;
import vip.gpg123.umami.service.WebsiteService;
import vip.gpg123.umami.mapper.WebsiteMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
* @author gaopuguang
* @description 针对表【website】的数据库操作Service实现
* @createDate 2025-12-30 16:13:39
*/
@Service
public class WebsiteServiceImpl extends ServiceImpl<WebsiteMapper, Website> implements WebsiteService{

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("umami")
    public boolean save(Website entity) {
        return super.save(entity);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    @DS("umami")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    @DS("umami")
    public boolean updateById(Website entity) {
        return super.updateById(entity);
    }
}




