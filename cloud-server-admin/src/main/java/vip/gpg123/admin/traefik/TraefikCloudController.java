package vip.gpg123.admin.traefik;

import cn.hutool.core.convert.Convert;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.core.domain.AjaxResult;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.api.model.networking.v1.IngressList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/10/12 23:49
 **/
@RestController
@RequestMapping("/traefikCloud")
@Api(tags = "traefik云原生版")
public class TraefikCloudController {

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        IngressList list = K8sUtil.createKClient().network().v1().ingresses().list();
        List<Ingress> ingressList = list.getItems();
        return AjaxResult.success(ingressList);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> ingressList = Convert.toList(list().get("data"));
        return PageUtils.toPage(ingressList);
    }
}
