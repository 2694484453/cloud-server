package vip.gpg123.devops;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ScalableResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author gaopuguang
 * @date 2024/11/11 0:18
 **/
@RestController
@RequestMapping("/devops/job")
@Api(tags = "【devops】流水线任务管理")
public class JobController {

    @Qualifier("KubernetesClient")
    @Autowired
    private KubernetesClient client;


    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overView() {
        JobList jobList = client.batch().v1().jobs().inAnyNamespace().list();
        List<Job> jobs = jobList.getItems();
        AtomicReference<Integer> actives = new AtomicReference<>(0);
        AtomicReference<Integer> fails = new AtomicReference<>(0);
        AtomicReference<Integer> successes = new AtomicReference<>(0);
        // 今日新增
        jobs.forEach(job -> {
            // 激活次数
            if (ObjectUtil.isNull(job.getStatus().getActive()) || (ObjectUtil.isNotNull(job.getStatus().getActive()) && job.getStatus().getActive() == 1)) {
                actives.getAndSet(actives.get() + 1);
            }
            // 失败次数
            if (ObjectUtil.isNull(job.getStatus().getFailed()) || (ObjectUtil.isNotNull(job.getStatus().getFailed()) && job.getStatus().getFailed() == 1)) {
                actives.getAndSet(fails.get() + 1);
            }
            // 成功次数
            if (ObjectUtil.isNull(job.getStatus().getSucceeded()) || (ObjectUtil.isNotNull(job.getStatus().getSucceeded()) && job.getStatus().getSucceeded() == 1)) {
                successes.getAndSet(successes.get() + 1);
            }
        });
        Map<String, Object> map = new HashMap<>();
        // job总数
        map.put("jobs", jobList.getItems().size());
        // 激活数
        map.put("actives", actives);
        // 失败数
        map.put("fails", fails);
        // 成功数
        map.put("successes", successes);
        return AjaxResult.success(map);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<?> jobs = getJobs();
        return AjaxResult.success(jobs);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> jobs = getJobs();
        return PageUtils.toPage(jobs);
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
                           @RequestParam("nameSpace") String nameSpace) {
        Job job = client.batch().v1().jobs().inNamespace(nameSpace).withName(jobName).get();
        return ObjectUtil.isNotEmpty(job) ? AjaxResult.success("查询成功", job) : AjaxResult.success("查询成功", new ArrayList<Job>());
    }

    /**
     * 创建一个job
     *
     * @return r
     */
    @GetMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestParam("jobName") String jobName,
                          @RequestParam("nameSpace") String nameSpace) {
        // 执行创建
        Job job = client.batch().v1().jobs().inNamespace(nameSpace).withName(jobName).create();
        return AjaxResult.success("操作成功", job);
    }

    /**
     * 执行
     *
     * @param jobName 名称
     * @return r
     */
    @PostMapping("/run")
    @ApiOperation(value = "执行")
    public AjaxResult run(@RequestParam("jobName") String jobName,
                          @RequestParam("nameSpace") String nameSpace) {
        //
        client.batch().v1().jobs().inNamespace(nameSpace).withName(jobName).scale(0);
        //
        client.batch().v1().jobs().inNamespace(nameSpace).withName(jobName).scale(1);
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
                          @RequestParam("nameSpace") String nameSpace) {
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
                              @RequestParam(value = "nameSpace") String nameSpace) {
        // 用于创建一个 SSE 连接对象
        SseEmitter emitter = new SseEmitter();
        ThreadUtil.execute(() -> {
            try {
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

    /**
     * 获取job
     *
     * @return r
     */
    private List<?> getJobs() {
        List<Job> jobList;
        try {
            jobList = client.batch().v1().jobs().inAnyNamespace().list().getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jobList;
    }
}
