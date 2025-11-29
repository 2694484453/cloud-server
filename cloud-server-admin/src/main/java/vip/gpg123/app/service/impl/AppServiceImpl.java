package vip.gpg123.app.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.service.AppService;
import vip.gpg123.app.service.MineAppService;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.helm.service.impl.HelmApiServiceImpl;
import vip.gpg123.system.domain.SysActionNotice;
import vip.gpg123.system.service.SysActionNoticeService;

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

    @Autowired
    private SysActionNoticeService sysActionNoticeService;

    private static final String ROUTING_KEY = "cloud-server-email";

    /**
     * 安装
     *
     * @param namespace   命名空间
     * @param releaseName 名称
     * @param chartUrl    url
     * @param kubeContext kubeContext
     * @return r
     */
    @Override
    public String install(String releaseName, String namespace, String chartUrl, String values, String kubeContext) {
        // 新增
        MineApp mineApp = new MineApp();
        mineApp.setAppName(releaseName);
        mineApp.setChartUrl(chartUrl);
        mineApp.setGitUrl("");
        mineApp.setSource("");
        mineApp.setStatus("installing");
        mineApp.setValue(values);
        mineApp.setTags("");
        mineApp.setDescription("");
        mineApp.setCreateBy(SecurityUtils.getUsername());
        mineApp.setCreateTime(DateUtil.date());
        mineApp.setNameSpace(namespace);
        mineApp.setReleaseName(releaseName);
        mineApp.setKubeContext(kubeContext);
        String res;
        try {
            boolean isSaved = mineAppService.save(mineApp);
            if (!isSaved) {
                throw new RuntimeException("保存失败");
            }
            // 安装
            res = super.install(releaseName, namespace, chartUrl, values, kubeContext);
            mineApp.setStatus("installed");
            mineApp.setResult(res);
        } catch (Exception e) {
            mineApp.setStatus("installFailed");
            throw new RuntimeException(e);
        } finally {
            mineAppService.updateById(mineApp);
            // 获取用户邮箱
            String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
            // 获取用户名
            String userName = SecurityUtils.getLoginUser().getUser().getUserName();
            // 异步消息任务
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    String title = "应用安装通知";
                    String content = "安装" + chartUrl + ",结果：" + mineApp.getStatus();
                    // 站内消息
                    SysActionNotice sysActionNotice = new SysActionNotice();
                    sysActionNotice.setTitle(title);
                    sysActionNotice.setCreateBy(userName);
                    sysActionNotice.setCreateTime(DateUtil.date());
                    sysActionNotice.setContent(content);
                    sysActionNotice.setType("helmInstall");
                    sysActionNotice.setSendType("email");
                    sysActionNotice.setToUser(userName);
                    sysActionNotice.setToAddress(userEmail);
                    sysActionNoticeService.save(sysActionNotice);
                    // 组装消息
//                    Email email = new Email();
//                    String[] tos = new String[]{};
//                    tos = ArrayUtil.append(tos, userEmail);
//                    email.setTos(tos);
//                    email.setTitle(title);
//                    email.setContent(content);
//                    rabbitTemplate.convertAndSend(ROUTING_KEY, email);
                }
            });
        }
        return res;
    }


    /**
     * 卸载
     *
     * @param namespace   命名空间
     * @param releaseName 卸载名称
     * @param kubeContext HelmApp helmApp
     */
    @Override
    public String uninstall(String namespace, String releaseName, String kubeContext) {
        // 查询
        MineApp mineApp = mineAppService.getOne(new LambdaQueryWrapper<MineApp>()
                .eq(MineApp::getReleaseName, releaseName)
                .eq(MineApp::getKubeContext, kubeContext)
                .eq(MineApp::getCreateBy, SecurityUtils.getUsername())
        );
        String res = "";
        if (ObjectUtil.isNotNull(mineApp)) {
            try {
                mineApp.setStatus("uninstalling");
                res = super.uninstall(namespace, releaseName, kubeContext);
                // 删除
                mineAppService.removeById(mineApp);
                mineApp.setResult(res);
            } catch (Exception e) {
                mineApp.setStatus("uninstallFailed");
                mineAppService.updateById(mineApp);
                throw new RuntimeException(e);
            } finally {
                // 获取用户邮箱
                String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
                // 获取用户名
                String userName = SecurityUtils.getLoginUser().getUser().getUserName();
                // 异步消息任务
                AsyncManager.me().execute(new TimerTask() {
                    @Override
                    public void run() {
                        String title = "应用卸载通知";
                        String content = "卸载" + releaseName + ",结果：" + mineApp.getStatus();
                        // 站内消息
                        SysActionNotice sysActionNotice = new SysActionNotice();
                        sysActionNotice.setTitle(title);
                        sysActionNotice.setCreateBy(userName);
                        sysActionNotice.setCreateTime(DateUtil.date());
                        sysActionNotice.setContent(content);
                        sysActionNotice.setType("helmUninstall");
                        sysActionNotice.setSendType("email");
                        sysActionNotice.setToUser(userName);
                        sysActionNotice.setToAddress(userEmail);
                        sysActionNoticeService.save(sysActionNotice);
//                        // 组装消息
//                        Email email = new Email();
//                        String[] tos = new String[]{};
//                        tos = ArrayUtil.append(tos, userEmail);
//                        email.setTos(tos);
//                        email.setTitle(title);
//                        email.setContent(content);
//                        rabbitTemplate.convertAndSend(ROUTING_KEY, email);
                    }
                });
            }
        }
        return res;
    }
}
