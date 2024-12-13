package com.ruoyi.devops;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/12/3 23:34
 **/
@RestController
@RequestMapping("/podLog")
@Api(tags = "pod控制管理")
public class PodController {

    /**
     * 列表查询
     *
     * @param podName   pod
     * @param nameSpace ns
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "podName", required = false) String podName,
                           @RequestParam(value = "nameSpace", required = false) String nameSpace) {
        List<Pod> pods = K8sUtil.createKClient().pods().inAnyNamespace().list().getItems();
        return AjaxResult.success("查询成功", pods);
    }

    /**
     * 分页查询
     * @param podName pod
     * @param nameSpace ns
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "podName", required = false) String podName,
                              @RequestParam(value = "nameSpace", required = false) String nameSpace) {
        List<?> list = Convert.toList(list(podName, nameSpace).get("data"));
        return PageUtils.toPage(list);
    }

    /**
     * 获取日志
     * @param podName pd
     * @param nameSpace ns
     * @return r
     */
    @GetMapping("/logs")
    @ApiOperation(value = "日志")
    public SseEmitter podLogs(@RequestParam(value = "podName") String podName,
                              @RequestParam(value = "nameSpace") String nameSpace){
        // 用于创建一个 SSE 连接对象
        SseEmitter emitter = new SseEmitter();
        ThreadUtil.execute(()->{
            try {
               PodResource podResource = K8sUtil.createKClient().pods().inNamespace(nameSpace).withName(podName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(podResource.watchLog().getOutput()));
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
