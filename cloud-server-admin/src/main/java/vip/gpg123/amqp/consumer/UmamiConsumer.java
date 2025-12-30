package vip.gpg123.amqp.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.umami.domain.UmamiUser;
import vip.gpg123.umami.domain.Website;
import vip.gpg123.umami.service.UmamiUserService;
import vip.gpg123.umami.service.WebsiteService;

import java.util.Map;

@Component
public class UmamiConsumer {

    public static final String umami = "cloud-server-umami";

    @Autowired
    private UmamiUserService umamiUserService;

    @Autowired
    private WebsiteService websiteService;

    /**
     * 创建user
     * @param sysUser s
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = umami, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = umami), // 声明直接交换器
            key = "createUser" // 定义路由键
    ))
    public void createUser(SysUser sysUser) {
        UmamiUser umamiUser = new UmamiUser();
        BeanUtils.copyProperties(sysUser, umamiUser);
        umamiUser.setUserId(String.valueOf(sysUser.getUserId()));
        umamiUser.setUsername(sysUser.getUserName());
        umamiUser.setPassword(sysUser.getPassword());
        umamiUser.setCreatedAt(sysUser.getCreateTime());
        umamiUser.setUpdatedAt(sysUser.getUpdateTime());
        umamiUser.setRole("user");
        umamiUserService.save(umamiUser);
    }

    /**
     * create
     * @param map m
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = umami, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = umami), // 声明直接交换器
            key = "createWebsite" // 定义路由键
    ))
    public void createWebsite(Map<String, Object> map) {
        Website website = new Website();
        BeanUtils.copyProperties(map, website);
        websiteService.save(website);
    }

    /**
     * update
     * @param map m
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = umami, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = umami), // 声明直接交换器
            key = "updateWebsite" // 定义路由键
    ))
    public void updateWebsite(Map<String, Object> map) {
        Website website = new Website();
        BeanUtils.copyProperties(map, website);
        websiteService.updateById(website);
    }

    /**
     * delete
     * @param map m
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = umami, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = umami), // 声明直接交换器
            key = "deleteWebsite" // 定义路由键
    ))
    public void deleteWebsite(Map<String, Object> map) {
        Website website = new Website();
        BeanUtils.copyProperties(map, website);
        websiteService.removeById(website);
    }
}
