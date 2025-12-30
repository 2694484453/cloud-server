package vip.gpg123.umami.service;

import vip.gpg123.umami.domain.Session;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author gaopuguang
* @description 针对表【session】的数据库操作Service
* @createDate 2025-12-19 03:07:34
*/
public interface SessionService extends IService<Session> {

    /**
     * 访客
     * @param startAt s
     * @param endAt e
     * @return r
     */
    List<Map<String,Object>> metrics(String startAt, String endAt);

    /**
     * 访客
     * @param startAt s
     * @param endAt e
     * @return r
     */
    List<Map<String,Object>> chinaMetrics(String startAt, String endAt);

    /**
     * vist
     * @param startAt s
     * @param endAt e
     * @return r
     */
    long visitors(String startAt, String endAt);
}
