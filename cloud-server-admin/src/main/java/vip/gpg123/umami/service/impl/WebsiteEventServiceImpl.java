package vip.gpg123.umami.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.gpg123.dashboard.domain.TodayView;
import vip.gpg123.umami.domain.WebsiteEvent;
import vip.gpg123.umami.service.WebsiteEventService;
import vip.gpg123.umami.mapper.WebsiteEventMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【website_event】的数据库操作Service实现
 * @createDate 2025-12-19 03:49:53
 */
@Service
public class WebsiteEventServiceImpl extends ServiceImpl<WebsiteEventMapper, WebsiteEvent> implements WebsiteEventService {

    @Autowired
    private WebsiteEventMapper websiteEventMapper;

    @Value("${analytics.umami.website-id}")
    private String websiteId;

    @Override
    @DS("umami")
    public int visits(LocalDateTime start, LocalDateTime end) {
        return websiteEventMapper.visits(start, end, websiteId);
    }

    @Override
    @DS("umami")
    public long pageViews(LocalDateTime start, LocalDateTime end) {
        return websiteEventMapper.selectCount(new LambdaQueryWrapper<WebsiteEvent>().eq(WebsiteEvent::getWebsiteId, websiteId).between(WebsiteEvent::getCreatedAt, start, end));
    }

    @Override
    @DS("umami")
    public List<TodayView> todayViews() {
        return websiteEventMapper.todyVisitView(websiteId);
    }

    @DS("umami")
    public List<TodayView> getWebsiteEvents() {
        return websiteEventMapper.todyVisitView(websiteId);
    }

}




