package vip.gpg123.app;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.HelmEntity;
import vip.gpg123.app.service.impl.HelmApi;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.service.KubernetesClusterService;

import java.util.List;

@RestController
@RequestMapping("/app/manager")
public class HelmAppManagerController extends BaseController {

    @Autowired
    private KubernetesClusterService kubernetesClusterService;

    @Autowired
    private HelmApi helmApi;

    /**
     * list
     *
     * @param entity e
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(HelmEntity entity) {
        return AjaxResult.success(listApps(entity));
    }

    /**
     * page
     *
     * @param entity e
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(HelmEntity entity) {
        JSONArray jsonArray = listApps(entity);
        List<?> list = jsonArray.toList(Object.class);
        return PageUtils.toPage(list);
    }

    public JSONArray listApps(HelmEntity entity) {
        JSONArray jsonArray = new JSONArray();
        List<KubernetesCluster> list = kubernetesClusterService.list(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getStatus, "ok")
                .eq(KubernetesCluster::getCreateBy, getUserId())
                .eq(StrUtil.isNotBlank(entity.getKubeContext()), KubernetesCluster::getContextName, entity.getKubeContext())
        );
        list.forEach(kubernetesCluster -> {
            entity.setKubeContext(kubernetesCluster.getContextName());
            JSONArray res = helmApi.listJsonArray(entity);
            jsonArray.addAll(res);
        });
        return jsonArray;
    }
}
