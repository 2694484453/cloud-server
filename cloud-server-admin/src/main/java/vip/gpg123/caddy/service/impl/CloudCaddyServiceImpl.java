package vip.gpg123.caddy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.caddy.domain.CloudCaddy;
import vip.gpg123.caddy.service.CaddyApi;
import vip.gpg123.caddy.service.CloudCaddyService;
import vip.gpg123.caddy.mapper.CloudCaddyMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.manager.AsyncManager;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【cloud_caddy(caddy)】的数据库操作Service实现
 * @createDate 2025-12-02 23:06:47
 */
@Service
public class CloudCaddyServiceImpl extends ServiceImpl<CloudCaddyMapper, CloudCaddy> implements CloudCaddyService {

    @Autowired
    private CaddyApi caddyApi;

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(CloudCaddy entity) {
        boolean result = super.save(entity);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 重载
                caddyApi.load();
            }
        });
        return result;
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 重载
                caddyApi.load();
            }
        });
        return result;
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean updateById(CloudCaddy entity) {
        boolean result = super.updateById(entity);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 重载
                caddyApi.load();
            }
        });
        return result;
    }
}




