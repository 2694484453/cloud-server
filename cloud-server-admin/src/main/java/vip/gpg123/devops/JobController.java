package vip.gpg123.devops;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.PodResource;
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
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

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
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<Job> jobs = client.batch().v1().jobs().inNamespace("default").list().getItems();
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
        AjaxResult ajaxResult = list();
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }

    /**
     * 查询详情
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam("name") String name) {
        Job job = client.batch().v1().jobs().inNamespace("default").withName(name).get();
        if (ObjectUtil.isNotEmpty(job)) {
            return AjaxResult.success("查询成功", job);
        }
        return AjaxResult.success("查询成功", null);
    }

    /**
     * 创建一个job
     *
     * @return r
     */
    @GetMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestParam("name") String name) {
        // 执行创建
        Job job = K8sUtil.createKClient().batch().v1().jobs().inNamespace("default").withName(name).create();
        return AjaxResult.success("操作成功", job);
    }

    /**
     * 执行
     *
     * @param name 名称
     * @return r
     */
    @PostMapping("/run")
    @ApiOperation(value = "执行")
    public AjaxResult run(@RequestParam("name") String name) {
        //
        client.batch().v1().jobs().inNamespace("default").withName(name).scale(0);
        //
        client.batch().v1().jobs().inNamespace("default").withName(name).scale(1);
        return AjaxResult.success("操作成功", true);
    }

    /**
     * 获取日志
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/log")
    @ApiOperation(value = "日志流")
    public AjaxResult log(@RequestParam("name") String name) {
        String logs = client.batch().v1().jobs().inNamespace("default").withName(name).getLog(true);
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
}
