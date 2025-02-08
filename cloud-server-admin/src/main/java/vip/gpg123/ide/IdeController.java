package vip.gpg123.ide;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/9 1:26
 **/
@RestController
@RequestMapping("/ide")
@Api(tags = "【ide】管理")
public class IdeController {

    @Value("${repo.helm.name}")
    private String repoName;

    @Value("${repo.helm.url}")
    private String repoUrl;

    private static final String version = "1.0.0";

    private static final String nameSpace = "ide";

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> list = ides();
        return PageUtils.toPage(list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<?> list = ides();
        return AjaxResult.success(list);
    }

    /**
     * 部署
     *
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "部署")
    public AjaxResult add(@RequestParam("releaseName") String releaseName) {
        // 初始化命令
        String[] init = {};
        // 参数
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.set("user", SecurityUtils.getUsername());
        jsonObject.set("env", null);
        jsonObject.set("init", null);
        //
        init = ArrayUtil.append(init, "helm", "install", "--set", "user=" + SecurityUtils.getUsername(), releaseName, repoName + "/ide/code-server", "-v", version, "-n", nameSpace);
        String result = RuntimeUtil.execForStr(StandardCharsets.UTF_8, init);
        return AjaxResult.success("操作成功", result);
    }

    private List<?> ides() {
        List<?> list;
        String user = SecurityUtils.getUsername();
        try (KubernetesClient kubernetesClient = K8sUtil.createKClient()) {
            list = kubernetesClient.apps().deployments().inNamespace("ide").withLabel(user).list().getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
