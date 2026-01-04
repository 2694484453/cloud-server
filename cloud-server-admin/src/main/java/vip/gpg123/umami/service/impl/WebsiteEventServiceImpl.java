package vip.gpg123.umami.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.dashboard.domain.TodayView;
import vip.gpg123.framework.config.UmamiConfig;
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

    @Autowired
    private UmamiConfig.umamiCloudWebProperties umamiCloudWebProperties;

    @Override
    @DS("umami")
    public int visits(LocalDateTime start, LocalDateTime end) {
        return websiteEventMapper.visits(start, end, umamiCloudWebProperties.getWebsiteId());
    }

    @Override
    @DS("umami")
    public long pageViews(LocalDateTime start, LocalDateTime end) {
        return websiteEventMapper.selectCount(new LambdaQueryWrapper<WebsiteEvent>().eq(WebsiteEvent::getWebsiteId, umamiCloudWebProperties.getWebsiteId()).between(WebsiteEvent::getCreatedAt, start, end));
    }

    @Override
    @DS("umami")
    public List<TodayView> todayViews() {
        return websiteEventMapper.todyVisitView(umamiCloudWebProperties.getWebsiteId());
    }

    @DS("umami")
    public List<TodayView> getWebsiteEvents() {
        return websiteEventMapper.todyVisitView(umamiCloudWebProperties.getWebsiteId());
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类 {@link QueryWrapper}
     */
    @Override
    @DS("umami")
    public int count(Wrapper<WebsiteEvent> queryWrapper) {
        return super.count(queryWrapper);
    }

    /**
     * 查询总记录数
     *
     * @see Wrappers#emptyWrapper()
     */
    @Override
    @DS("umami")
    public int count() {
        return super.count();
    }
}




