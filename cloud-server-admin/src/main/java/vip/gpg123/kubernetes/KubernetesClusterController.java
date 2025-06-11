package vip.gpg123.kubernetes;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.client.Config;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.service.KubernetesClusterService;
import vip.gpg123.kubernetes.vo.KubernetesClusterVo;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/kubernetesCluster")
@Api(tags = "集群服务管理")
public class KubernetesClusterController extends BaseController {

    @Autowired
    private KubernetesClusterService kubernetesClusterService;

    /**
     * 列表查询
     *
     * @param clusterName 名称
     * @param status      状态
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(value = "clusterName", required = false) String clusterName,
                           @RequestParam(value = "status", required = false) String status) {
        List<KubernetesCluster> list = kubernetesClusterService.list(new LambdaQueryWrapper<KubernetesCluster>()
                .like(StrUtil.isNotBlank(clusterName), KubernetesCluster::getClusterName, clusterName)
                .eq(StrUtil.isNotBlank(status), KubernetesCluster::getStatus, status)
                .eq(KubernetesCluster::getCreateBy, getUsername())
                .orderByDesc(KubernetesCluster::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param clusterName 名称
     * @param status      状态
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(value = "clusterName", required = false) String clusterName,
                              @RequestParam(value = "status", required = false) String status) {
        IPage<KubernetesCluster> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = kubernetesClusterService.page(page, new LambdaQueryWrapper<KubernetesCluster>()
                .like(StrUtil.isNotBlank(clusterName), KubernetesCluster::getClusterName, clusterName)
                .eq(StrUtil.isNotBlank(status), KubernetesCluster::getStatus, status)
                .eq(KubernetesCluster::getCreateBy, getUsername())
                .orderByDesc(KubernetesCluster::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 添加集群
     * @param kubernetesClusterVo vo
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【添加集群】")
    public AjaxResult add(@RequestBody KubernetesClusterVo kubernetesClusterVo) {
        // 检查配置文件
        if (kubernetesClusterVo.getFile() == null) {
            return AjaxResult.error("无效的配置文件！");
        }
        Config config = null;
        // 解析文件
        try {
            // 尝试转换
            config = K8sUtil.parseConfig(kubernetesClusterVo.getFile());
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        } finally {
            // 转换完成后
           if (config != null) {
               KubernetesCluster kubernetesCluster = new KubernetesCluster();
               BeanUtils.copyProperties(kubernetesClusterVo, kubernetesCluster);
               // 进行保存
               kubernetesCluster.setCreateBy(getUsername());
               kubernetesCluster.setCreateTime(DateUtil.date());
               kubernetesCluster.setConfig(FileUtil.readString(kubernetesClusterVo.getFile(), StandardCharsets.UTF_8));
               kubernetesClusterService.save(kubernetesCluster);
           }
        }
        return AjaxResult.error("未知原因");
    }
}
