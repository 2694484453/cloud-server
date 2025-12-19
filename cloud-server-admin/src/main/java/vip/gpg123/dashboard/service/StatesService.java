package vip.gpg123.dashboard.service;

import vip.gpg123.dashboard.domain.States;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gaopuguang
* @description 针对表【states】的数据库操作Service
* @createDate 2025-12-19 16:33:06
*/
public interface StatesService extends IService<States> {

    /**
     * 通过code查询
     * @param code c
     * @return r
     */
    States selectByIsoCode(String code);

}
