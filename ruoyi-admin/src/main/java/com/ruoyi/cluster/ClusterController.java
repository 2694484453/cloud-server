package com.ruoyi.cluster;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.NamedAuthInfo;
import io.fabric8.kubernetes.api.model.NamedCluster;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.framework.qual.RequiresQualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Qualifier("defaultConfigPath")
    @Autowired
    private String configPath;

    /**
     * 集群列表
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    private AjaxResult list() {
        List<NamedCluster> clusterList;
        try {
            // 配置文件信息
            File file = FileUtil.file(configPath);
            Config config = KubeConfigUtils.parseConfig(file);
            clusterList = config.getClusters();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(clusterList);
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
            File configFile = FileUtil.file(configPath);
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
            KubeConfigUtils.persistKubeConfigIntoFile(config, configPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success("添加成功");
    }

    /**
     * 删除
     * @param name 名称
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam("name") String name) {
        try {
            // 配置文件信息
            File configFile = FileUtil.file(configPath);
            Config config = KubeConfigUtils.parseConfig(configFile);
            // 集群
            List<NamedCluster> clusters = config.getClusters();
            List<NamedCluster> newClusters = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(clusters)) {
                clusters.forEach(e->{
                    if (!name.equals(e.getName())){
                        newClusters.add(e);
                    }
                });
            }
            // context
            List<NamedContext> contexts = config.getContexts();
            List<NamedContext> newContexts = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(contexts)) {
                contexts.forEach(e->{
                    if (!name.equals(e.getName())) {
                        newContexts.add(e);
                    }
                });
            }
            // users
            List<NamedAuthInfo> users = config.getUsers();
            List<NamedAuthInfo> newUsers = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(users)) {
                users.forEach(e->{
                    if (!name.equals(e.getName())) {
                        newUsers.add(e);
                    }
                });
            }
            config.setClusters(newClusters);
            config.setContexts(newContexts);
            config.setUsers(newUsers);
            KubeConfigUtils.persistKubeConfigIntoFile(config,configPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success("删除成功");
    }
}

