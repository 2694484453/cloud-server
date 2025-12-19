package vip.gpg123.dashboard.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.gpg123.dashboard.domain.Session;
import vip.gpg123.dashboard.service.DouAreaWorldService;
import vip.gpg123.dashboard.service.SessionService;
import vip.gpg123.dashboard.mapper.SessionMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.dashboard.service.StatesService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @description 针对表【session】的数据库操作Service实现
 * @createDate 2025-12-19 03:07:34
 */
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session> implements SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private DouAreaWorldService douAreaWorldService;

    @Autowired
    private StatesService statesService;

    @Value("${analytics.umami.website-id}")
    private String websiteId;

    /**
     * 访客
     *
     * @param startAt s
     * @param endAt   e
     * @return r
     */
    @Override
    @DS("umami")
    public List<Map<String, Object>> metrics(String startAt, String endAt) {
        LocalDateTime start = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(startAt)),
                ZoneId.systemDefault() // 使用系统默认时区，如 Asia/Shanghai
        );
        LocalDateTime end = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(endAt)),
                ZoneId.systemDefault() // 使用系统默认时区，如 Asia/Shanghai
        );
        List<Map<String, Object>> dataList = new LinkedList<>();
        List<Map<String, Object>> result = sessionMapper.countByCountry(start, end, websiteId);
        result.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            if (ObjectUtil.isNotEmpty(item)) {
                String country = String.valueOf(item.get("country"));
                map.put("name", StrUtil.isNotBlank(country) ? douAreaWorldService.selectByCountryCode2(country).getName() : null);
                map.put("value", item.get("count"));
            } else {
                map.put("name", null);
                map.put("value", null);
            }
            dataList.add(map);
        });
        return dataList;
    }

    /**
     * 访客
     *
     * @param startAt s
     * @param endAt   e
     * @return r
     */
    @Override
    @DS("umami")
    public List<Map<String, Object>> chinaMetrics(String startAt, String endAt) {
        LocalDateTime start = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(startAt)),
                ZoneId.systemDefault() // 使用系统默认时区，如 Asia/Shanghai
        );
        LocalDateTime end = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(endAt)),
                ZoneId.systemDefault() // 使用系统默认时区，如 Asia/Shanghai
        );
        List<Map<String, Object>> dataList = new LinkedList<>();
        List<Map<String, Object>> result = sessionMapper.countByRegion(start, end, websiteId, "CN");
        result.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            if (ObjectUtil.isNotNull(item)) {
                if (item.containsKey("region")) {
                    String region = String.valueOf(item.get("region"));
                    map.put("name", StrUtil.isNotBlank(region) ? statesService.selectByIsoCode(region).getCname() : null);
                    map.put("value", item.get("count"));
                }
            }else {
                map.put("name", null);
                map.put("value", null);
            }
            dataList.add(map);
        });
        return dataList;
    }


    @Override
    @DS("umami")
    public long visitors(String startAt, String endAt) {
        LocalDateTime start = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(startAt)),
                ZoneId.systemDefault() // 使用系统默认时区，如 Asia/Shanghai
        );
        LocalDateTime end = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(endAt)),
                ZoneId.systemDefault() // 使用系统默认时区，如 Asia/Shanghai
        );
        return sessionMapper.selectCount(new LambdaQueryWrapper<Session>().eq(Session::getWebsiteId, websiteId).between(Session::getCreatedAt, start, end));
    }
}




