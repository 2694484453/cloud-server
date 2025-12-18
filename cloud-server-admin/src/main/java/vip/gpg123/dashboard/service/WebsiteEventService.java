package vip.gpg123.dashboard.service;

import vip.gpg123.dashboard.domain.WebsiteEvent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * @author gaopuguang
 * @description 针对表【website_event】的数据库操作Service
 * @createDate 2025-12-19 03:49:53
 */
public interface WebsiteEventService extends IService<WebsiteEvent> {

    int visits(LocalDateTime start, LocalDateTime end);

    long pageViews(LocalDateTime start, LocalDateTime end);
}
