package vip.gpg123.devops;

import cn.hutool.core.thread.ThreadUtil;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vip.gpg123.common.utils.K8sUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author gaopuguang
 * @date 2024/12/15 1:21
 **/
@RestController
@RequestMapping("/sse")
@Api(tags = "【devops】sse控制")
public class PodLogsSseController {

    /**
     * 获取日志
     * @param podName pd
     * @param nameSpace ns
     * @return r
     */
    @GetMapping("/podLogs")
    @ApiOperation(value = "日志")
    public SseEmitter podLogs(@RequestParam(value = "podName") String podName,
                              @RequestParam(value = "nameSpace") String nameSpace,
                              @RequestParam(value = "contextName") String contextName){
        // 用于创建一个 SSE 连接对象
        SseEmitter emitter = new SseEmitter();
        ThreadUtil.execute(()->{
            try {
                KubernetesClient client = K8sUtil.createKubernetesClient(contextName);
                PodResource podResource = client.pods().inNamespace(nameSpace).withName(podName);
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
