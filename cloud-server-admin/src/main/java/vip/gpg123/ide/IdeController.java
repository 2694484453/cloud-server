package vip.gpg123.ide;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/9 1:26
 **/
@RestController
@RequestMapping("/ide")
@Api(tags = "【ide】管理")
public class IdeController {

    /**
     * 分页查询
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
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(){
        List<?> list = ides();
        return AjaxResult.success(list);
    }

    private List<?> ides(){
        List<?> list;
        String user = SecurityUtils.getUsername();
        try(KubernetesClient kubernetesClient = K8sUtil.createKClient()) {
           list = kubernetesClient.apps().deployments().inNamespace("ide").withLabel(user).list().getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
