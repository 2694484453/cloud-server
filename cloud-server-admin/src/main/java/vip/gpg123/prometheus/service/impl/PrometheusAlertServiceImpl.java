package vip.gpg123.prometheus.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.prometheus.domain.PrometheusAlert;
import vip.gpg123.prometheus.dto.PrometheusAlertVO;
import vip.gpg123.prometheus.service.PrometheusAlertService;
import vip.gpg123.prometheus.mapper.PrometheusAlertMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_alert(告警消息)】的数据库操作Service实现
* @createDate 2025-12-14 21:14:02
*/
@Service
public class PrometheusAlertServiceImpl extends ServiceImpl<PrometheusAlertMapper, PrometheusAlert> implements PrometheusAlertService{

    @Autowired
    private PrometheusAlertMapper prometheusAlertMapper;

    /**
     * list
     *
     * @param alert a
     * @return r
     */
    @Override
    public List<PrometheusAlert> list(PrometheusAlert alert) {
        return prometheusAlertMapper.list(alert);
    }

    /**
     * page
     *
     * @param page  p
     * @param alert a
     * @return r
     */
    @Override
    public IPage<PrometheusAlert> page(Page<PrometheusAlert> page, PrometheusAlert alert) {
        return prometheusAlertMapper.page(page, alert);
    }

    /**
     * page
     *
     * @param page  p
     * @param alert a
     * @return r
     */
    @Override
    public IPage<PrometheusAlertVO> pageExtension(Page<PrometheusAlert> page, PrometheusAlert alert) {
        return prometheusAlertMapper.pageExtension(page, alert);
    }
}




