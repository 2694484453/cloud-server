package vip.gpg123.prometheus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.prometheus.domain.PrometheusAlert;
import vip.gpg123.prometheus.service.PrometheusAlertService;
import vip.gpg123.prometheus.mapper.PrometheusAlertMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【prometheus_alert(告警消息)】的数据库操作Service实现
* @createDate 2025-12-14 21:14:02
*/
@Service
public class PrometheusAlertServiceImpl extends ServiceImpl<PrometheusAlertMapper, PrometheusAlert>
    implements PrometheusAlertService{

}




