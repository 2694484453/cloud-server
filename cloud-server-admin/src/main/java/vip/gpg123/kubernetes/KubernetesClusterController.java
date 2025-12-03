package vip.gpg123.kubernetes;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.NamedAuthInfo;
import io.fabric8.kubernetes.api.model.NamedCluster;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.validator.routines.DomainValidator;
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
import vip.gpg123.kubernetes.mapper.KubernetesClusterMapper;
import vip.gpg123.kubernetes.service.KubernetesClusterService;
import vip.gpg123.kubernetes.vo.KubernetesClusterVo;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vip.gpg123.framework.datasource.DynamicDataSourceContextHolder.log;

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
     * @param kubernetesCluster k
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【添加集群】")
    public AjaxResult add(@RequestBody KubernetesCluster kubernetesCluster) {
        // 配置名称
        if (StrUtil.isBlank(kubernetesCluster.getClusterName())) {
            return AjaxResult.error("名称不能为空");
        }
        if (StrUtil.isNotBlank(kubernetesCluster.getClusterName())) {
            boolean res = DomainValidator.getInstance().isValid(kubernetesCluster.getClusterName());
            if (!res) {
                return AjaxResult.error("名称不是域名格式");
            }
        }
        // 检查配置内容
        if (StrUtil.isBlank(kubernetesCluster.getConfig())) {
            return AjaxResult.error("配置内容不能为空！");
        }
        // 解析文件
        try {
            // 尝试转换校验
            io.fabric8.kubernetes.api.model.Config addConfig = K8sUtil.getConfigFromStr(kubernetesCluster.getConfig());
            if (addConfig.getClusters().isEmpty()) {
                return AjaxResult.error("cluster配置不能为空");
            }
            if (addConfig.getClusters().size() > 1) {
                return AjaxResult.error("cluster配置数量不能大于1");
            }
            if (addConfig.getUsers().isEmpty()) {
                return AjaxResult.error("users配置不能为空");
            }
            if (addConfig.getUsers().size() > 1) {
                return AjaxResult.error("users配置数量不能大于1");
            }
            if (addConfig.getContexts().isEmpty()) {
                return AjaxResult.error("contexts配置不能为空");
            }
            if (addConfig.getContexts().size() > 1) {
                return AjaxResult.error("contexts配置数量不能大于1");
            }
            // 校验成功后
            NamedContext addName = addConfig.getContexts().get(0);
            addName.setName(kubernetesCluster.getClusterName());
            NamedAuthInfo addUser = addConfig.getUsers().get(0);
            addUser.setName(kubernetesCluster.getClusterName());
            NamedCluster addCluster = addConfig.getClusters().get(0);
            addCluster.setName(kubernetesCluster.getClusterName());
            //
            addConfig.setClusters(new ArrayList<NamedCluster>() {{
                add(addCluster);
            }});
            addConfig.setContexts(new ArrayList<NamedContext>() {{
                add(addName);
            }});
            addConfig.setUsers(new ArrayList<NamedAuthInfo>() {{
                add(addUser);
            }});
            // 读取默认配置
            io.fabric8.kubernetes.api.model.Config kubeConfig = K8sUtil.getDefaultConfig();
            kubeConfig.getClusters().add(addCluster);
            kubeConfig.getUsers().add(addUser);
            kubeConfig.getContexts().add(addName);
            //
            kubernetesCluster.setConfig(Serialization.yamlMapper().writeValueAsString(addConfig));
            kubernetesCluster.setCreateBy(String.valueOf(getUserId()));
            kubernetesCluster.setCreateTime(DateUtil.date());
            kubernetesClusterService.save(kubernetesCluster);
            // 重写默认文件
            K8sUtil.exportToFile(kubeConfig, K8sUtil.defaultConfigFilePath());
        } catch (Exception e) {
            return AjaxResult.error("验证失败：" + e.getMessage());
        }
        return AjaxResult.error("未知原因");
    }

    /**
     * 导出
     *
     * @param kubernetesClusterVo vo
     */
    @PostMapping("/export")
    @ApiOperation(value = "【导出】")
    public void exporter(KubernetesClusterVo kubernetesClusterVo, HttpServletResponse response) throws IOException {
        // 当前用户
        List<KubernetesCluster> list = kubernetesClusterService.list(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUserId())
        );

        io.fabric8.kubernetes.api.model.Config config = new io.fabric8.kubernetes.api.model.Config();
        // 组装
        list.forEach(kubernetesCluster -> {
            try {
                io.fabric8.kubernetes.api.model.Config config1 = K8sUtil.getConfigFromStr(kubernetesCluster.getConfig());
                config.getClusters().addAll(config1.getClusters());
                config.getUsers().addAll(config1.getUsers());
                config.getContexts().addAll(config1.getContexts());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        File file = FileUtil.createTempFile();
        try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            K8sUtil.exportToFile(config,file.getPath());
            // 设置响应
            response.setContentType("application/x-yaml");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"config\"");
            response.setCharacterEncoding("UTF-8");
            // 2. 带缓冲的流复制
            IoUtil.copy(FileUtil.getInputStream(file),out);
        } catch(Exception e) {
            log.error("文件导出失败：{}", e.getMessage());
            response.sendError(500, "文件导出失败：" + e.getMessage());
        } finally {
            FileUtil.del(file);
        }
    }

    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overView() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> total = new HashMap<>();
        // 总数
        total.put("title", "集群总数");
        total.put("count", kubernetesClusterService.count(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUserId())
        ));
        // 正常集群
        Map<String, Object> health = new HashMap<>();
        health.put("title", "正常集群数");
        health.put("count", kubernetesClusterService.count(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUserId())
                .eq(KubernetesCluster::getStatus, "ok")
        ));
        // 异常集群
        Map<String, Object> unHealth = new HashMap<>();
        unHealth.put("title", "异常集群数");
        unHealth.put("count", kubernetesClusterService.count(new LambdaQueryWrapper<KubernetesCluster>()
                .eq(KubernetesCluster::getCreateBy, getUsername())
                .eq(KubernetesCluster::getStatus, "error")
        ));
        list.add(total);
        list.add(health);
        list.add(unHealth);
        return AjaxResult.success(list);
    }
}
