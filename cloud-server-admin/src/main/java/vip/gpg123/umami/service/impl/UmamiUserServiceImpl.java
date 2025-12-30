package vip.gpg123.umami.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.umami.domain.UmamiUser;
import vip.gpg123.umami.service.UmamiUserService;
import vip.gpg123.umami.mapper.UmamiUserMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
* @author gaopuguang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-12-30 15:50:07
*/
@Service
public class UmamiUserServiceImpl extends ServiceImpl<UmamiUserMapper, UmamiUser> implements UmamiUserService{

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    @DS("umami")
    public boolean save(UmamiUser entity) {
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
    public boolean updateById(UmamiUser entity) {
        return super.updateById(entity);
    }
}




