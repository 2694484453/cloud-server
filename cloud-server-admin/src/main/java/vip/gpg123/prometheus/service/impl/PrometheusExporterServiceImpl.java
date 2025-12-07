package vip.gpg123.prometheus.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.gpg123.amqp.producer.PrometheusProducer;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.core.domain.model.LoginUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.service.PrometheusExporterService;
import vip.gpg123.prometheus.mapper.PrometheusExporterMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.message.MessageProducer;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【prometheus_exporter】的数据库操作Service实现
 * @createDate 2025-11-19 01:40:53
 */
@Service
public class PrometheusExporterServiceImpl extends ServiceImpl<PrometheusExporterMapper, PrometheusExporter> implements PrometheusExporterService {

    @Autowired
    private PrometheusProducer prometheusProducer;

    @Autowired
    private MessageProducer messageProducer;

    @Value("${monitor.prometheus.exporterPath}")
    private String exporterPath;

    private static final String modelName = "Prometheus端点";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(PrometheusExporter entity) {
        boolean result = super.save(entity);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 配置文件位置
                prometheusProducer.createExporterFile(entity);
                // 发送邮件
                EmailBody emailBody = new EmailBody();
                emailBody.setTos(new String[]{loginUser.getUser().getEmail()});
                emailBody.setName(entity.getJobName());
                emailBody.setAction("新增");
                emailBody.setModelName(modelName);
                messageProducer.sendEmail("新增", modelName, result, loginUser.getUser().getEmail(), true);
            }
        });
        return result;
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean updateById(PrometheusExporter entity) {
        boolean isSuccess = super.updateById(entity);
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                PrometheusExporter exporter = baseMapper.selectById(entity.getId());
                if (exporter != null) {
                    String oldPath = exporterPath + "/" + exporter.getExporterType() + "/" + exporter.getJobName() + ".json";
                    // 是否修改名称
                    if (!exporter.getJobName().equals(entity.getJobName())) {
                        String newPath = exporterPath + "/" + exporter.getExporterType() + "/" + entity.getJobName() + ".json";
                        FileUtil.rename(FileUtil.file(oldPath), newPath, true);
                    }
                    // 是否修改了类型
                    if (!exporter.getExporterType().equals(entity.getExporterType())) {
                        String newPath = exporterPath + "/" + entity.getExporterType() + "/" + entity.getJobName() + ".json";
                        FileUtil.move(FileUtil.file(oldPath), FileUtil.file(newPath), true);
                    }
                }
            }
        });
        return isSuccess;
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean flag = super.removeById(id);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                PrometheusExporter entity = baseMapper.selectById(id);
                if (entity != null) {
                    // 配置文件位置
                    String filePath = exporterPath + "/" + entity.getExporterType() + "/" + entity.getJobName() + ".json";
                    // 删除文件
                    FileUtil.del(filePath);
                    // 发送邮件
                    EmailBody emailBody = new EmailBody();
                    emailBody.setTos(new String[]{loginUser.getUser().getEmail()});
                    emailBody.setName(entity.getJobName());
                    emailBody.setAction("删除");
                    emailBody.setModelName(modelName);
                    emailBody.setResult(flag);
                    messageProducer.sendEmail(emailBody, true);
                }
            }
        });
        return flag;
    }
}




