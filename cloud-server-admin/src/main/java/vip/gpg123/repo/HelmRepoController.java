package vip.gpg123.repo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
                        helmRepo.setUpdateResult(execRes);
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
                        // zhi
                        helmRepo.setUpdateResult(execRes);
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


//    /**
//     * 查询详情
//     *
//     * @param name    名称
//     * @param version 版本
//     * @param type    类型
//     * @return r
//     */
//    @GetMapping("info")
//    @ApiOperation(value = "查看详情")
//    public AjaxResult info(@RequestParam(value = "name") String name,
//                           @RequestParam(value = "version", required = false) String version,
//                           @RequestParam(value = "type", required = false, defaultValue = "values") String type) {
//        String[] init = {"helm", "show"};
//        switch (type) {
//            case "values":
//                init = ArrayUtil.append(init, "values", repoName + "/" + name);
//                break;
//            case "readme":
//                init = ArrayUtil.append(init, "readme", repoName + "/" + name);
//        }
//        String res = RuntimeUtil.execForStr(init);
//        return null;
//    }
}
