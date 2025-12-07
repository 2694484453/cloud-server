package vip.gpg123.devops;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ScalableResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.devops.domain.DevopsJob;
import vip.gpg123.devops.mapper.DevopsJobMapper;
import vip.gpg123.devops.service.DevopsJobService;
import vip.gpg123.devops.service.DevopsTaskBuildService;
import vip.gpg123.devops.service.DevopsTaskGitService;
import vip.gpg123.kubernetes.domain.KubernetesCluster;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author gaopuguang
 * @date 2024/11/11 0:18
 **/
@RestController
@RequestMapping("/devops/job")
@Api(tags = "【devops】流水线任务管理")
public class DevopsJobController extends BaseController {

    @Autowired
    private DevopsJobService devopsJobService;

    @Autowired
    private DevopsJobMapper devopsJobMapper;

    @Autowired
    private DevopsTaskGitService devopsTaskGitService;

    @Autowired
    private DevopsTaskBuildService devopsTaskBuildService;

    private static final String nameSpace = "devops";

    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overView() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> actives = new HashMap<>();
        actives.put("title", "当前运行中");
        actives.put("count", devopsJobService.count(new LambdaQueryWrapper<DevopsJob>()
                .eq(DevopsJob::getCreateBy, getUserId())
                .eq(DevopsJob::getStatus, "running")));
        list.add(actives);

        Map<String, Object> noActive = new HashMap<>();
        noActive.put("title", "未运行");
        noActive.put("count", devopsJobService.count(new LambdaQueryWrapper<DevopsJob>()
                .eq(DevopsJob::getCreateBy, getUserId())
                .eq(DevopsJob::getStatus, "")));
        list.add(noActive);

        // job总数
        Map<String, Object> map = new HashMap<>();
        map.put("title", "我的job总数");
        map.put("count", devopsJobService.count(new LambdaQueryWrapper<DevopsJob>()
                .eq(DevopsJob::getCreateBy, getUserId())));
        list.add(map);
        return AjaxResult.success(list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "nameSpace", required = false) String nameSpace) {
        List<?> jobs = devopsJobService.list(new LambdaQueryWrapper<DevopsJob>()
                .eq(DevopsJob::getCreateBy, getUserId())
                .like(StrUtil.isNotBlank(name), DevopsJob::getJobName, name)
                .like(StrUtil.isNotBlank(nameSpace), DevopsJob::getNameSpace, nameSpace)
                .orderByDesc(DevopsJob::getCreateTime)
        );
        return AjaxResult.success(jobs);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "nameSpace", required = false) String nameSpace) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<DevopsJob> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        DevopsJob devopsJob = new DevopsJob();
        devopsJob.setJobName(name);
        List<DevopsJob> list = devopsJobMapper.page(pageDomain, devopsJob);
        page.setRecords(list);
        page.setTotal(devopsJobMapper.list(devopsJob).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 查询详情
     *
     * @param jobName   名称
     * @param nameSpace 名称
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam("jobName") String jobName,
                           @RequestParam("nameSpace") String nameSpace,
                           @RequestParam("contextName") String contextName) {
        KubernetesClient client = K8sUtil.createKubernetesClient(contextName);
        Job job = client.batch().v1().jobs().inNamespace(nameSpace).withName(jobName).get();
        return ObjectUtil.isNotEmpty(job) ? AjaxResult.success("查询成功", job) : AjaxResult.success("查询成功", new ArrayList<Job>());
    }

    /**
     * 创建一个job
     *
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody DevopsJob devopsJob) {
        if (StrUtil.isBlank(devopsJob.getJobName())) {
            return AjaxResult.error("任务名称不能为空");
        }
        if (StrUtil.isBlank(devopsJob.getContextName())) {
            return AjaxResult.error("contextName不能为空");
        }
        devopsJob.setCreateBy(String.valueOf(getUserId()));
        devopsJob.setCreateTime(new Date());
        boolean result = devopsJobService.save(devopsJob);
        // 执行创建
        return result ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 修改
     * @param devopsJob d
     * @return r
     */
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody DevopsJob devopsJob) {
        if (StrUtil.isBlank(devopsJob.getJobName())) {
            return AjaxResult.error("任务名称不能为空");
        }else if (!K8sUtil.checkResourceName(devopsJob.getJobName())) {
            return AjaxResult.error("任务名称不合规范");
        }
        if (StrUtil.isBlank(devopsJob.getContextName())) {
            return AjaxResult.error("contextName不能为空");
        }
        devopsJob.setUpdateBy(String.valueOf(getUserId()));
        devopsJob.setUpdateTime(new Date());
        return devopsJobService.updateById(devopsJob) ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    /**
     * 执行
     *
     * @param devopsJob dev
     * @return r
     */
    @PostMapping("/exec")
    @ApiOperation(value = "执行")
    public AjaxResult run(@RequestBody DevopsJob devopsJob) {
        if (StrUtil.isBlank(devopsJob.getJobName())) {
            return AjaxResult.error("任务名称不能为空");
        }
        if (StrUtil.isBlank(devopsJob.getContextName())) {
            return AjaxResult.error("contextName不能为空");
        }
        // 查询集群是否存在任务
        try {
            KubernetesClient kubernetesClient = K8sUtil.createKubernetesClient(devopsJob.getContextName());
            Job job = kubernetesClient.batch().v1().jobs().inNamespace(nameSpace).withName(devopsJob.getJobName()).get();
            if (ObjectUtil.isNotNull(job)) {
                kubernetesClient.batch().v1().jobs().inNamespace(nameSpace).withName(devopsJob.getJobName()).scale(1);
            } else {
                // 重新创建资源
                Job newJob = new Job();
                ObjectMeta meta = job.getMetadata();
                meta.setNamespace(nameSpace);
                meta.setName(devopsJob.getJobName());
                newJob.setMetadata(meta);
                kubernetesClient.batch().v1().jobs().create(newJob);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success("操作成功", true);
    }

    /**
     * 获取日志
     *
     * @param jobName 名称
     * @return r
     */
    @GetMapping("/log")
    @ApiOperation(value = "日志流")
    public AjaxResult log(@RequestParam("jobName") String jobName,
                          @RequestParam("nameSpace") String nameSpace,
                          @RequestParam("contextName") String contextName) {
        KubernetesClient client = K8sUtil.createKubernetesClient(contextName);
        String logs = client.batch().v1().jobs().inNamespace(nameSpace).withName(jobName).getLog(true);
        return AjaxResult.success(logs);
    }

    /**
     * 获取日志
     *
     * @param jobName   jn
     * @param nameSpace ns
     * @return r
     */
    @GetMapping("/jobLogs")
    @ApiOperation(value = "日志")
    public SseEmitter podLogs(@RequestParam(value = "jobName") String jobName,
                              @RequestParam(value = "nameSpace") String nameSpace,
                              @RequestParam(value = "contextName") String contextName) {
        // 用于创建一个 SSE 连接对象
        SseEmitter emitter = new SseEmitter();
        ThreadUtil.execute(() -> {
            try {
                KubernetesClient client = K8sUtil.createKubernetesClient(contextName);
                ScalableResource<Job> job = client.batch().v1().jobs().inNamespace(nameSpace).withName(jobName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(job.watchLog().getOutput()));
                String line;
                while ((line = reader.readLine()) != null) {
                    emitter.send(line);
                }
                // 数据发送完成后，关闭连接
                emitter.complete();
            } catch (Exception e) {
                // 发生错误时，关闭连接并报错
                emitter.completeWithError(e);
            } finally {

            }
        });
        return emitter;
    }

}
