package vip.gpg123.kubernetes;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.service.KubernetesClusterService;

import java.util.List;

@RestController
@RequestMapping("/kubernetes/server")
@Api(tags = "集群服务管理")
public class KubernetesClusterController extends BaseController {

    @Autowired
    private KubernetesClusterService kubernetesClusterService;

    /**
     * 列表查询
     *
     * @param name   名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "status", required = false) String status) {
        List<KubernetesCluster> list = kubernetesClusterService.list(new LambdaQueryWrapper<KubernetesCluster>()
                .like(KubernetesCluster::getContextName, name)
                .eq(KubernetesCluster::getStatus, status)
                .eq(KubernetesCluster::getCreateBy, getUsername())
                .orderByDesc(KubernetesCluster::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param name 名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "status", required = false) String status) {
        IPage<KubernetesCluster> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = kubernetesClusterService.page(page, new LambdaQueryWrapper<KubernetesCluster>()
                .like(KubernetesCluster::getContextName, name)
                .eq(KubernetesCluster::getStatus, status)
                .eq(KubernetesCluster::getCreateBy, getUsername())
                .orderByDesc(KubernetesCluster::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }
}
