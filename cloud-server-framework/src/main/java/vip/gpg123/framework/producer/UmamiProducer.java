package vip.gpg123.framework.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.common.core.domain.entity.SysUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UmamiProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String umami = "cloud-server-umami";

    /**
     * 创建用户
     *
     * @param sysUser s
     */
    public void createUser(SysUser sysUser) {
        rabbitTemplate.convertAndSend(umami, "createUser", sysUser);
        System.out.println("创建umami用户: " + sysUser);
    }

    /**
     * 创建网站
     */
    public void createWebsite(String domain, String domainId, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("domain", domain);
        map.put("name", domain);
        map.put("websiteId", domainId);
        map.put("createBy", userId);
        map.put("createdAt", new Date());
        map.put("userId", userId);
        map.put("shareId", userId);
        rabbitTemplate.convertAndSend(umami, "createWebsite", map);
        System.out.println("创建umami站点: " + domain);
    }

    /**
     * 更新网站
     */
    public void updateWebsite(String domain, String domainId, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("domain", domain);
        map.put("name", domain);
        map.put("websiteId", domainId);
        map.put("createBy", userId);
        map.put("updatedAt", new Date());
        map.put("userId", userId);
        map.put("shareId", userId);
        rabbitTemplate.convertAndSend(umami, "updateWebsite", map);
        System.out.println("更新umami站点: " + domain);
    }

    /**
     * 删除网站
     */
    public void deleteWebsite(String domainId) {
        rabbitTemplate.convertAndSend(umami, "deleteWebsite", domainId);
        System.out.println("删除umami站点: " + domainId);
    }

}
