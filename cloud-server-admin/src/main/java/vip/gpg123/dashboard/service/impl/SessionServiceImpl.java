package vip.gpg123.dashboard.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.dashboard.domain.Session;
import vip.gpg123.dashboard.service.SessionService;
import vip.gpg123.dashboard.mapper.SessionMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        List<Session> list = sessionMapper.selectList(new LambdaQueryWrapper<Session>()
                .between(Session::getCreatedAt, start, end)
        );
        list.forEach(session -> {
            Map<String, Object> map = new HashMap<>();
            map.put("x", session.getCountry());
            map.put("y", sessionMapper.selectCount(new LambdaQueryWrapper<Session>().eq(Session::getCountry, session.getCountry()).between(Session::getCreatedAt, start, end)));
            dataList.add(map);
        });
        return dataList;
    }
}




