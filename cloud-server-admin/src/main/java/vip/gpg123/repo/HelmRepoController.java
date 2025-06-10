package vip.gpg123.repo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.DumperOptions;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.helm.HelmUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.repo.domain.HelmRepo;
import vip.gpg123.repo.domain.HelmRepoItem;
import vip.gpg123.repo.domain.HelmRepoResponse;
import vip.gpg123.repo.service.HelmRepoApiService;
import vip.gpg123.repo.service.HelmRepoService;
import vip.gpg123.repo.vo.HelmRepoConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static vip.gpg123.framework.datasource.DynamicDataSourceContextHolder.log;

/**
 * @author gaopuguang
 * @date 2024/8/30 23:50
 **/
@RestController
@RequestMapping("/helmRepo")
public class HelmRepoController extends BaseController {

    @Autowired
    private HelmRepoApiService helmRepoApiService;

    @Autowired
    private HelmRepoService helmRepoService;

    /**
     * 查询list
     *
     * @param helmRepo repo
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(HelmRepo helmRepo) {
        // 查询仓库
       List<HelmRepo> list = helmRepoService.list(new LambdaQueryWrapper<HelmRepo>()
                .like(StrUtil.isNotBlank(helmRepo.getRepoName()), HelmRepo::getRepoName, helmRepo.getRepoName())
                .eq(StrUtil.isNotBlank(helmRepo.getStatus()), HelmRepo::getStatus, helmRepo.getStatus())
                .eq(HelmRepo::getCreateBy, getUsername())
                .orderByDesc(HelmRepo::getCreateTime)
        );
       return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param helmRepo repo
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(HelmRepo helmRepo) {
        IPage<HelmRepo> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = helmRepoService.page(page, new LambdaQueryWrapper<HelmRepo>()
                .like(StrUtil.isNotBlank(helmRepo.getRepoName()), HelmRepo::getRepoName, helmRepo.getRepoName())
                .eq(StrUtil.isNotBlank(helmRepo.getStatus()), HelmRepo::getStatus, helmRepo.getStatus())
                .eq(HelmRepo::getCreateBy, getUsername())
                .orderByDesc(HelmRepo::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 添加仓库
     * @param helmRepo repo
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增仓库")
    public AjaxResult add(@RequestBody @Valid HelmRepo helmRepo) {
        // 执行添加
        String addRes = HelmUtils.repoAdd(helmRepo.getRepoName(), helmRepo.getRepoUrl());
        if (StrUtil.isNotBlank(addRes) && !addRes.contains("Error")) {
            // 查询是否存在
            int searchCount = helmRepoService.count(new LambdaQueryWrapper<HelmRepo>()
                    .eq(HelmRepo::getRepoName, helmRepo.getRepoName())
            );
            if (searchCount > 0) {
                return AjaxResult.error("已存在相同的名称仓库，请勿重复添加或更换名称！");
            }
            helmRepo.setCreateBy(getUsername());
            helmRepo.setCreateTime(DateUtil.date());
            boolean isSaved = helmRepoService.save(helmRepo);
            if (isSaved) {
                return AjaxResult.success(addRes);
            }
            return AjaxResult.error("保存失败",true);
        }
        return AjaxResult.error(addRes, false);
    }

    /**
     * 更新仓库
     * @param repoName repo
     * @return r
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新仓库")
    public AjaxResult update(@RequestParam(value = "repoName", required = false) String repoName) {
        String userName = getUsername();
        // 根据名称查询仓库信息
        HelmRepo helmRepo = helmRepoService.getOne(new LambdaQueryWrapper<HelmRepo>()
                .eq(HelmRepo::getRepoName, repoName));
        // 查询到结果
        if (ObjectUtil.isNotNull(helmRepo)) {
            helmRepo.setStatus("updating");
            helmRepo.setUpdateTime(DateUtil.date());
            helmRepo.setUpdateBy(userName);
            helmRepoService.updateById(helmRepo);
            // 异步任务处理
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    int chartNum = 0;
                    AtomicInteger chartVersionNum = new AtomicInteger();
                    String execRes = null;
                    try {
                        execRes = HelmUtils.repoUpdate(repoName);
                        helmRepo.setExecResult(execRes);
                        // 如果成功更新仓库数据
                        if (StrUtil.isNotBlank(execRes) && execRes.contains("Successfully")) {
                            // 请求仓库列表
                            String res = helmRepoApiService.index(URI.create(helmRepo.getRepoUrl()));
                            HelmRepoResponse repoResponse = YamlUtil.load(StrUtil.getReader(res), HelmRepoResponse.class);
                            Map<String, List<HelmRepoItem>> entries = repoResponse.getEntries();
                            chartNum = entries.size();
                            entries.forEach((k, v) -> {
                                // 一个key就是一个应用，list数量即为版本数量
                                chartVersionNum.set(chartVersionNum.get() + v.size());
                            });
                            helmRepo.setStatus("success");
                        }
                    } catch (Exception e) {
                        helmRepo.setStatus("fail");
                        execRes = e.getMessage();
                    } finally {
                        // 执行结果
                        helmRepo.setExecResult(execRes);
                        helmRepo.setArtifactTotal(chartNum);
                        helmRepo.setArtifactVersionTotal(chartVersionNum.get());
                        helmRepo.setUpdateBy(userName);
                        helmRepo.setRepoUpdateTime(DateUtil.date());
                        helmRepoService.updateById(helmRepo);
                    }
                }
            });
            return AjaxResult.success("正在更新中，请稍等片刻后刷新页面！");
        }
        return AjaxResult.error("未查询到仓库");
    }

    /**
     * 删除仓库
     * @param repoName repo
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除仓库")
    public AjaxResult delete(@RequestParam(value = "repoName", required = false) String repoName) {
        String userName = getUsername();
        // 根据名称查询仓库信息
        HelmRepo helmRepo = helmRepoService.getOne(new LambdaQueryWrapper<HelmRepo>()
                .eq(HelmRepo::getRepoName, repoName));
        // 查询到结果
        if (ObjectUtil.isNotNull(helmRepo)) {
            helmRepo.setStatus("deleting");
            helmRepo.setUpdateTime(DateUtil.date());
            helmRepo.setUpdateBy(userName);
            helmRepoService.updateById(helmRepo);
            // 执行异步
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    String execRes = null;
                    try {
                        execRes = HelmUtils.repoRemove(repoName);
                        if (StrUtil.isNotBlank(execRes) && execRes.contains("removed")) {
                           helmRepo.setStatus("deleteSuccess");
                        }
                    } catch (Exception e) {
                        helmRepo.setStatus("deleteFail");
                    } finally {
                        helmRepo.setExecResult(execRes);
                        helmRepoService.removeById(helmRepo.getId());
                    }
                }
            });
            return AjaxResult.success("正在删除中，请稍等片刻后刷新页面！");
        } else {
            return AjaxResult.error("未查询到仓库");
        }
    }


    /**
     * 导出仓库配置
     *
     * @param repoName   名称
     * @return r
     */
    @PostMapping("export")
    @ApiOperation(value = "导出仓库配置")
    public void export(@RequestParam(value = "repoName", required = false) String repoName,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        // 查询
        List<HelmRepo> list = helmRepoService.list(new LambdaQueryWrapper<HelmRepo>()
                .eq(HelmRepo::getCreateBy, getUsername())
                .like(StrUtil.isNotBlank(repoName), HelmRepo::getRepoName, repoName)
        );
        List<HelmRepoConfig.HelmRepoConfigItem> items = list.stream().map(e->{
            HelmRepoConfig.HelmRepoConfigItem item = new HelmRepoConfig.HelmRepoConfigItem();
            BeanUtils.copyProperties(e, item);
            item.setInsecure_skip_tls_verify(false);
            item.setPass_credentials_all(false);
            item.setName(e.getRepoName());
            item.setUrl(e.getRepoUrl());
            return item;
        }).collect(Collectors.toList());
        // 临时文件
        File file = FileUtil.createTempFile();
        FileWriter fileWriter = new FileWriter(file);
        try (OutputStream out = new BufferedOutputStream(response.getOutputStream())){
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            dumperOptions.setPrettyFlow(true);

            YamlUtil.dump(new HelmRepoConfig("", DateUtil.date().toDateStr(), items), fileWriter ,dumperOptions);
            // 1. 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("repositories.yaml", "UTF-8"));
            // 2. 带缓冲的流复制
            IoUtil.copy(FileUtil.getInputStream(file), out);
        } catch (Exception e) {
            log.error("文件导出失败：{}", e.getMessage());
            response.sendError(500, "文件导出失败：" + e.getMessage());
        } finally {
            IoUtil.close(fileWriter);
            FileUtil.del(file);
        }
    }
}
