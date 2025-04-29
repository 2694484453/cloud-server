package vip.gpg123.app.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.service.AppService;
import vip.gpg123.app.service.MineAppService;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.domain.Email;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.helm.service.impl.HelmApiServiceImpl;

import java.util.TimerTask;

/**
 * 应用
 */
@Service
public class AppServiceImpl extends HelmApiServiceImpl implements AppService {

    @Autowired
    private MineAppService mineAppService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 安装
     *
     * @param namespace   命名空间
     * @param repoName    仓库名称
     * @param chartName   chart名称
     * @param version     版本
     * @param kubeContext kubeContext
     */
    @Override
    public void install(String namespace, String repoName, String chartName, String version, String kubeContext) {
        // 新增
        MineApp mineApp = new MineApp();
        mineApp.setAppName(chartName);
        mineApp.setChartName(chartName);
        mineApp.setGitUrl("");
        mineApp.setSource("");
        mineApp.setStatus("installing");
        mineApp.setValue("");
        mineApp.setTags("");
        mineApp.setDescription("");
        mineApp.setCreateBy(SecurityUtils.getUsername());
        mineApp.setCreateTime(DateUtil.date());
        mineApp.setNameSpace(namespace);
        mineApp.setReleaseName(chartName);
        mineApp.setClusterName(SecurityUtils.getUsername());

        try {
            boolean isSaved = mineAppService.save(mineApp);
            if (!isSaved) {
                throw new RuntimeException("保存失败");
            }
            // 安装
            super.install(namespace, repoName, chartName, version, kubeContext);
            mineApp.setStatus("installed");
        } catch (Exception e) {
            mineApp.setStatus("installFailed");
            throw new RuntimeException(e);
        } finally {
            mineAppService.updateById(mineApp);
            // 获取用户邮箱
            String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
            // 异步消息任务
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    // 组装消息
                    Email email = new Email();
                    String[] tos = new String[]{};
                    tos = ArrayUtil.append(tos, userEmail);
                    email.setTos(tos);
                    email.setTitle("应用安装通知");
                    email.setContent("安装" + chartName + ",结果：" + mineApp.getStatus());
                    rabbitTemplate.convertAndSend("cloud-server-email", email);
                }
            });
        }
    }


    /**
     * 卸载
     *
     * @param namespace   命名空间
     * @param releaseName 卸载名称
     * @param kubeContext HelmApp helmApp
     */
    @Override
    public void uninstall(String namespace, String releaseName, String kubeContext) {
        // 查询
        MineApp mineApp = mineAppService.getOne(new LambdaQueryWrapper<MineApp>()
                .eq(MineApp::getReleaseName, releaseName)
                .eq(MineApp::getClusterName, SecurityUtils.getUsername())
                .eq(MineApp::getCreateBy, SecurityUtils.getUsername())
        );
        if (ObjectUtil.isNotNull(mineApp)) {
            try {
                mineApp.setStatus("uninstalling");
                super.uninstall(namespace, releaseName, kubeContext);
                // 删除
                mineAppService.removeById(mineApp);
            } catch (Exception e) {
                mineApp.setStatus("uninstallFailed");
                mineAppService.updateById(mineApp);
                throw new RuntimeException(e);
            } finally {
                // 获取用户邮箱
                String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
                // 异步消息任务
                AsyncManager.me().execute(new TimerTask() {
                    @Override
                    public void run() {
                        // 组装消息
                        Email email = new Email();
                        String[] tos = new String[]{};
                        tos = ArrayUtil.append(tos, userEmail);
                        email.setTos(tos);
                        email.setTitle("应用卸载通知");
                        email.setContent("卸载" + releaseName + ",结果：" + mineApp.getStatus());
                        rabbitTemplate.convertAndSend("cloud-server-email", email);
                    }
                });
            }
        }
    }
}
