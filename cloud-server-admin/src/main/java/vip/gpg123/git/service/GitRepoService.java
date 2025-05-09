package vip.gpg123.git.service;

import org.springframework.stereotype.Service;
import vip.gpg123.git.domain.GitRepo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;

/**
* @author Administrator
* @description 针对表【git】的数据库操作Service
* @createDate 2024-12-01 01:00:54
*/
public interface GitRepoService extends IService<GitRepo> {
    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    default boolean save(GitRepo entity) {
        return IService.super.save(entity);
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */
    @Override
    default boolean saveBatch(Collection<GitRepo> entityList) {
        return IService.super.saveBatch(entityList);
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Override
    default boolean saveOrUpdateBatch(Collection<GitRepo> entityList) {
        return IService.super.saveOrUpdateBatch(entityList);
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     * @param batchSize  每次的数量
     */
    @Override
    boolean saveOrUpdateBatch(Collection<GitRepo> entityList, int batchSize);

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     */
    @Override
    boolean saveOrUpdate(GitRepo entity);

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    default boolean removeById(Serializable id) {
        return IService.super.removeById(id);
    }
}
