package vip.gpg123.dashboard.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.gpg123.dashboard.domain.WebsiteEvent;
import vip.gpg123.dashboard.service.WebsiteEventService;
import vip.gpg123.dashboard.mapper.WebsiteEventMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author gaopuguang
* @description 针对表【website_event】的数据库操作Service实现
* @createDate 2025-12-19 03:49:53
*/
@Service
public class WebsiteEventServiceImpl extends ServiceImpl<WebsiteEventMapper, WebsiteEvent> implements WebsiteEventService{

    @Autowired
    private WebsiteEventMapper websiteEventMapper;

    @Value("${analytics.umami.website-id}")
    private String websiteId;

    @Override
    @DS("umami")
    public int visits(LocalDateTime start, LocalDateTime end) {
        return websiteEventMapper.visits(start,end,websiteId);
    }

    @Override
    @DS("umami")
    public long pageViews(LocalDateTime start, LocalDateTime end) {
        return websiteEventMapper.selectCount(new LambdaQueryWrapper<WebsiteEvent>().eq(WebsiteEvent::getWebsiteId, websiteId).between(WebsiteEvent::getCreatedAt, start, end));
    }


}




