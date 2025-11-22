package vip.gpg123.kubernetes;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
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
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.domain.KubernetesClusterConfig;
import vip.gpg123.kubernetes.domain.KubernetesContext;
import vip.gpg123.kubernetes.domain.KubernetesFileConfig;
import vip.gpg123.kubernetes.domain.KubernetesUser;
import vip.gpg123.kubernetes.mapper.KubernetesClusterMapper;
import vip.gpg123.kubernetes.service.KubernetesClusterService;
import vip.gpg123.kubernetes.vo.KubernetesClusterVo;
import vip.gpg123.vps.domain.CloudHostServer;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kubernetes/cluster")
@Api(tags = "集群服务管理")
public class KubernetesClusterController extends BaseController {

    @Autowired
    private KubernetesClusterService kubernetesClusterService;

    @Autowired
    private KubernetesClusterMapper kubernetesClusterMapper;

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
                .eq(KubernetesCluster::getCreateBy, getUserId())
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
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<KubernetesCluster> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        KubernetesCluster kubernetesCluster = new KubernetesCluster();
        kubernetesCluster.setClusterName(clusterName);
        kubernetesCluster.setStatus(status);
        kubernetesCluster.setCreateBy(String.valueOf(getUserId()));
        List<KubernetesCluster> list = kubernetesClusterMapper.page(pageDomain, kubernetesCluster);
        page.setTotal(kubernetesClusterMapper.list(kubernetesCluster).size());
        page.setRecords(list);
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 添加集群
     *
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
                kubernetesCluster.setCreateBy(String.valueOf(getUserId()));
                kubernetesCluster.setCreateTime(DateUtil.date());
                kubernetesCluster.setConfig(FileUtil.readString(kubernetesClusterVo.getFile(), StandardCharsets.UTF_8));
                kubernetesClusterService.save(kubernetesCluster);
            }
        }
        return AjaxResult.error("未知原因");
    }

    /**
     * 导出
     * @param kubernetesClusterVo vo
     */
    @PostMapping("/exporter")
    @ApiOperation(value = "【导出】")
    public void exporter(KubernetesClusterVo kubernetesClusterVo, HttpServletResponse response) {
        // 当前用户
        List<KubernetesCluster> list = kubernetesClusterService.list(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUsername())
        );
        KubernetesFileConfig config = new KubernetesFileConfig();

        List<KubernetesClusterConfig> clusters = new ArrayList<>();

        List<KubernetesUser> users = new ArrayList<>();

        List<KubernetesContext> contexts = new ArrayList<>();

        // 组装
        list.forEach(kubernetesCluster -> {
            String clusterName = kubernetesCluster.getClusterName();
            String configStr = kubernetesCluster.getConfig();
            JSONObject jsonObject = YamlUtil.load(IoUtil.toStream(configStr,StandardCharsets.UTF_8), JSONObject.class);
            KubernetesFileConfig kubernetesFileConfig = JSONUtil.toBean(jsonObject, KubernetesFileConfig.class);
                    //YamlUtil.load(IoUtil.toStream(configStr, StandardCharsets.UTF_8), KubernetesFileConfig.class);
            clusters.addAll(kubernetesFileConfig.getClusters());
            users.addAll(kubernetesFileConfig.getUsers());
            contexts.addAll(kubernetesFileConfig.getContexts());
        });
        config.setApiVersion("v1");
        config.setKind("Config");
        config.setPreferences(new Object());
        config.setClusters(clusters);
        config.setUsers(users);
        config.setContexts(contexts);

        try {
            File file = FileUtil.createTempFile();
            FileWriter fw = new FileWriter(file);
            YamlUtil.dump(config,fw);
            // 设置响应
            response.setContentType("application/x-yaml");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"config\"");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(FileUtil.readString(file, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
