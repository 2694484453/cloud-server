package vip.gpg123.dashboard.service;

import vip.gpg123.dashboard.domain.DouAreaWorld;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gaopuguang
* @description 针对表【dou_area_world】的数据库操作Service
* @createDate 2025-12-19 15:40:57
*/
public interface DouAreaWorldService extends IService<DouAreaWorld> {

    /**
     * 通过code2查询
     * @param code2 c
     * @return r
     */
    DouAreaWorld selectByCountryCode2(String code2);
}
