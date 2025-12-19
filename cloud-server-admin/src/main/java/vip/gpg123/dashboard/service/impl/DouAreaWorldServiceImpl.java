package vip.gpg123.dashboard.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.dashboard.domain.DouAreaWorld;
import vip.gpg123.dashboard.service.DouAreaWorldService;
import vip.gpg123.dashboard.mapper.DouAreaWorldMapper;
import org.springframework.stereotype.Service;

/**
 * @author gaopuguang
 * @description 针对表【dou_area_world】的数据库操作Service实现
 * @createDate 2025-12-19 15:40:57
 */
@Service
public class DouAreaWorldServiceImpl extends ServiceImpl<DouAreaWorldMapper, DouAreaWorld> implements DouAreaWorldService {

    @Autowired
    private DouAreaWorldMapper douAreaWorldMapper;

    /**
     * 通过code2查询
     *
     * @param code2 c
     * @return r
     */
    @Override
    @DS("map")
    public DouAreaWorld selectByCountryCode2(String code2) {
        return douAreaWorldMapper.selectCountryByCode2(code2);
    }
}




