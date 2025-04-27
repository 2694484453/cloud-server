package vip.gpg123.repo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.domain.Email;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.common.utils.helm.HelmApp;
import vip.gpg123.common.utils.helm.HelmUtils;

import java.util.List;
import java.util.TimerTask;

/**
 * @author gpg
 * @date 2025/4/20
 */
@RestController
@RequestMapping("/helm")
@Api(value = "Helm安装列表")
public class HelmApiController {

    @Value("${repo.helm.url}")
    private String url;

    @Value("${repo.helm.name}")
    private String repoName;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 列表查询
     * @param name name
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name",required = false) String name) {
        List<HelmApp> list = HelmUtils.list("", SecurityUtils.getUsername());
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param name name
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name",required = false) String name) {
        return PageUtils.toPage(ListUtil.toList(HelmUtils.list("", SecurityUtils.getUsername())));
    }

    /**
     * 安装
     * @param name name
     * @param helmApp app
     * @return r
     */
    @PostMapping("/install")
    @ApiOperation(value = "安装")
    public AjaxResult install(@RequestParam(value = "name") String name,@RequestBody HelmApp helmApp) {
        HelmUtils.install(name, repoName, name,"", SecurityUtils.getUsername());
        String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 组装消息
                Email email = new Email();
                String[] tos = new String[]{};
                tos = ArrayUtil.append(tos, userEmail);
                email.setTos(tos);
                email.setTitle("应用安装通知");
                email.setContent("安装"+name+",结果："+ helmApp.getStatus());
                rabbitTemplate.convertAndSend("cloud-server-email", email);
            }
        });
        return AjaxResult.success();
    }
}
