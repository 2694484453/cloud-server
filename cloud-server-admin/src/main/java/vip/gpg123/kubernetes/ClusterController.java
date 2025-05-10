package vip.gpg123.kubernetes;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.NamedAuthInfo;
import io.fabric8.kubernetes.api.model.NamedCluster;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.gpg123.kubernetes.service.ClusterService;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.config.KubernetesClientConfig;

import javax.servlet.ServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/9/6 22:39
 **/
@RestController
@RequestMapping("/cluster")
@Api(value = "集群管理")
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    /**
     * 集群列表
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    private AjaxResult list() {
        return AjaxResult.success("查询成功", clusterService.list());
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    private TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<?> clusters = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(clusters);
    }

    /**
     * 获取详情
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情")
    private AjaxResult info(@RequestParam(value = "name", required = false) String name) {
        name = StrUtil.isBlank(name) ? SecurityUtils.getUsername() : name;
        return AjaxResult.success(clusterService.one(name));
    }

    /**
     * 新增集群
     *
     * @param file file
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    private AjaxResult add(@RequestPart MultipartFile file) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 配置文件信息
            File configFile = FileUtil.file(KubernetesClientConfig.defaultConfigPath());
            Config config = KubeConfigUtils.parseConfig(configFile);
            // 临时文件
            File tempFile = FileUtil.createTempFile();
            inputStream = file.getInputStream();
            outputStream = FileUtil.getOutputStream(tempFile);
            IoUtil.copy(inputStream, outputStream);
            Config tempConfig = KubeConfigUtils.parseConfig(tempFile);
            // 添加集群
            List<NamedCluster> clusters = config.getClusters();
            clusters.addAll(tempConfig.getClusters());
            // 添加context
            List<NamedContext> contexts = config.getContexts();
            contexts.addAll(tempConfig.getContexts());
            // 添加users
            List<NamedAuthInfo> users = config.getUsers();
            users.addAll(tempConfig.getUsers());
            // 重新写入
            KubeConfigUtils.persistKubeConfigIntoFile(config, KubernetesClientConfig.defaultConfigPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success("添加成功");
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出")
    public AjaxResult export(ServletResponse response, @RequestParam(value = "name", required = false) String name) {
        // 获取配置文件位置
        File configFile = FileUtil.file(KubernetesClientConfig.defaultConfigPath());
        try {
            OutputStream outputStream = response.getOutputStream();
            // 不指定名字默认为全部
            if (StrUtil.isBlank(name)) {
                InputStream inputStream = null;
                inputStream = FileUtil.getInputStream(configFile);
                IoUtil.copy(inputStream, outputStream);
                IoUtil.close(inputStream);
            } else {
                // 否则获取部分
                Config resultConfig = new Config();
                Config config = KubeConfigUtils.parseConfig(configFile);
                config.getClusters().forEach(e -> {
                    if (name.equals(e.getName())) {

                    }
                });
            }
            IoUtil.close(outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 删除
     *
     * @param name 名称
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam("name") String name) {
        try {
            // 配置文件信息
            File configFile = FileUtil.file(KubernetesClientConfig.defaultConfigPath());
            Config config = KubeConfigUtils.parseConfig(configFile);
            // 集群
            List<NamedCluster> clusters = config.getClusters();
            List<NamedCluster> newClusters = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(clusters)) {
                clusters.forEach(e -> {
                    if (!name.equals(e.getName())) {
                        newClusters.add(e);
                    }
                });
            }
            // context
            List<NamedContext> contexts = config.getContexts();
            List<NamedContext> newContexts = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(contexts)) {
                contexts.forEach(e -> {
                    if (!name.equals(e.getName())) {
                        newContexts.add(e);
                    }
                });
            }
            // users
            List<NamedAuthInfo> users = config.getUsers();
            List<NamedAuthInfo> newUsers = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(users)) {
                users.forEach(e -> {
                    if (!name.equals(e.getName())) {
                        newUsers.add(e);
                    }
                });
            }
            config.setClusters(newClusters);
            config.setContexts(newContexts);
            config.setUsers(newUsers);
            KubeConfigUtils.persistKubeConfigIntoFile(config, KubernetesClientConfig.defaultConfigPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success("删除成功");
    }
}

