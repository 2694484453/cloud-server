package vip.gpg123.prometheus.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.gpg123.common.core.domain.model.LoginUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.service.PrometheusExporterService;
import vip.gpg123.prometheus.mapper.PrometheusExporterMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.common.service.EmailService;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【prometheus_exporter】的数据库操作Service实现
 * @createDate 2025-11-19 01:40:53
 */
@Service
public class PrometheusExporterServiceImpl extends ServiceImpl<PrometheusExporterMapper, PrometheusExporter> implements PrometheusExporterService {

    @Autowired
    private EmailService emailService;

    @Value("${monitor.prometheus.exporterPath}")
    private String exporterPath;

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(PrometheusExporter entity) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 配置文件位置
                String filePath = exporterPath + "/" + entity.getExporterType() + "/" + entity.getJobName() + ".json";
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("labels", new HashMap<String, String>() {{
                    put("job", entity.getJobName());
                }});
                jsonObject.put("targets", new ArrayList<String>() {{
                    add(entity.getTargets());
                }});
                jsonArray.add(jsonObject);
                String jsonFormat = JSONUtil.toJsonStr(jsonArray);
                // 写入
                FileUtil.writeString(jsonFormat, filePath, StandardCharsets.UTF_8);
                // 发送邮件
                emailService.sendSimpleMail("cloud-server云服务平台-添加监控端点", "添加监控端点：" + entity.getJobName(), loginUser.getUser().getEmail());
            }
        });
        return super.save(entity);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
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
                    emailService.sendSimpleMail("cloud-server云服务平台-删除监控端点", "删除监控端点：" + entity.getJobName(), loginUser.getUser().getEmail());
                }
            }
        });
        return super.removeById(id);
    }
}




