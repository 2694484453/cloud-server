package com.ruoyi.devops;

import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.framework.config.PodLogWebSocketServer;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.dsl.PodResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;

/**
 * @author gaopuguang
 * @date 2024/12/3 23:34
 **/
@RestController
@RequestMapping("/podLog")
public class PodLogController {

    @PostMapping("/get")
    public String sendMessage(@RequestParam(value = "id") String id,
                              @RequestParam(value = "podName") String podName,
                              @RequestParam(value = "nameSpace") String nameSpace) {
        // 获取参数
        OutputStream outputStream = null;
//        InputStream inputStream = null;
        try {
            PodResource podResource = K8sUtil.createKClient().pods().inNamespace(nameSpace).withName(podName);
            LogWatch logWatch = podResource.watchLog();
            //PodLogWebSocketServer.sendMessageSteamToClient(id, podResource.getLogInputStream());
            //PodLogWebSocketServer.sendMessageSteamToClient("D:\\log.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //IoUtil.close(outputStream);
            //IoUtil.close(inputStream);
        }
        return "Message sent to client with ID: " + id;
    }
}
