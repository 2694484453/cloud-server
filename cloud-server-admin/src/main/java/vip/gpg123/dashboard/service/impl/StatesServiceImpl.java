package vip.gpg123.dashboard.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.dashboard.domain.States;
import vip.gpg123.dashboard.mapper.StatesMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.dashboard.service.StatesService;

/**
 * @author gaopuguang
 * @description 针对表【states】的数据库操作Service实现
 * @createDate 2025-12-19 16:33:06
 */
@Service
public class StatesServiceImpl extends ServiceImpl<StatesMapper, States> implements StatesService {

    @Autowired
    private StatesMapper statesMapper;

    /**
     * 通过code查询
     *
     * @param code c
     * @return r
     */
    @Override
    @DS("map")
    public States selectByIsoCode(String code) {
        return statesMapper.selectByIsoCode(code);
    }
}




