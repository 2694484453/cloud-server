package vip.gpg123.discovery;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.discovery.domain.NacosResponse;
import vip.gpg123.discovery.domain.NacosService;
import vip.gpg123.discovery.vo.NameSpace;
import vip.gpg123.discovery.service.NacosApiService;
import vip.gpg123.domain.Email;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.notice.domain.SysActionNotice;
import vip.gpg123.notice.service.SysActionNoticeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

@RestController
@RequestMapping("/nacos")
@Api(tags = "【nacos】服务管理")
public class NacosManagerController extends BaseController {

    @Autowired
    private NacosApiService nacosApiService;

    @Autowired
    private SysActionNoticeService sysActionNoticeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String ROUTING_KEY = "cloud-server-email";

    /**
     * 检查命名空间
     * @return r
     */
    @GetMapping("/check")
    @ApiOperation(value = "检查")
    public AjaxResult check() {
        // 获取用户名
        String nameSpaceId = getUsername().replaceAll("\\.", "-");
        // 查询是否有命名空间
        NacosResponse<NameSpace> response = nacosApiService.namespaces();
        if (response.getCode() != 200) {
            return AjaxResult.error("nacos服务异常");
        }
        List<NameSpace> data = response.getData();
        Map<String, NameSpace> map = new HashMap<>();
        CollectionUtil.toMap(data, map, NameSpace::getNamespace);
        if (!map.containsKey(nameSpaceId)) {
            // 自动创建ns
            Boolean result = nacosApiService.createNs(new HashMap<String, String>() {{
                    put("customNamespaceId",nameSpaceId);
                    put("namespaceName",nameSpaceId);
                    put("namespaceDesc","自动创建");
            }});
            if (result) {
                // 获取用户邮箱
                String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
                // 获取用户名
                String userName = SecurityUtils.getLoginUser().getUser().getUserName();
                // 异步消息
                AsyncManager.me().execute(new TimerTask() {
                    @Override
                    public void run() {
                        // 发送邮件
                        String title = "Nacos命名空间创建通知";
                        String content = "创建" + nameSpaceId + ",结果：" + result;
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
                        Email email = new Email();
                        String[] tos = new String[]{};
                        tos = ArrayUtil.append(tos, userEmail);
                        email.setTos(tos);
                        email.setTitle(title);
                        email.setContent(content);
                        rabbitTemplate.convertAndSend(ROUTING_KEY, email);
                    }
                });
            }
        }
        return AjaxResult.success();
    }

    /**
     * 获取命名空间列表
     * @param name 名称
     * @return r
     */
    @GetMapping("/service/list")
    @ApiOperation(value = "列表")
    public AjaxResult list(@RequestParam(value = "name",required = false) String name) {
        // 获取用户名
        String nameSpaceId = getUsername().replaceAll("\\.", "-");
        NacosService response = nacosApiService.service(nameSpaceId, 1, 9999);
        List<String> data = response.getDoms();
        return AjaxResult.success(data);
    }

    /**
     * 分页
     * @param name 名称
     * @return r
     */
    @GetMapping("/service/page")
    @ApiOperation(value = "分页")
    public TableDataInfo page(@RequestParam(value = "name",required = false) String name) {
        // 获取用户名
        String nameSpaceId = getUsername().replaceAll("\\.", "-");
        NacosService response = nacosApiService.service(nameSpaceId, TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        List<String> data = response.getDoms();
        if (StrUtil.isNotBlank(name)) {
            data = CollectionUtil.filter(data, item -> item.contains(name));
        }
        return PageUtils.toPage(data);
    }
}
