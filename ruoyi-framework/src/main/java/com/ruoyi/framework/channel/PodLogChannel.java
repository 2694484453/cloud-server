package com.ruoyi.framework.channel;

import cn.hutool.core.thread.ThreadUtil;
import com.ruoyi.common.utils.K8sUtil;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 使用 @ServerEndpoint 注解表示此类是一个 WebSocket 端点
// 通过 value 注解，指定 websocket 的路径
@ServerEndpoint(value = "/channel/podLogChannel/{id}")
@Component
public class PodLogChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(PodLogChannel.class);

    private Session session;

    // 收到消息
    @OnMessage
    public void onMessage(String message) throws IOException {
        LOGGER.info("[websocket] 收到消息：id={}，message={}", this.session.getId(), message);
        if (message.equalsIgnoreCase("bye")) {
            // 由服务器主动关闭连接。状态码为 NORMAL_CLOSURE（正常关闭）。
            this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Bye"));
            return;
        }
        this.session.getAsyncRemote().sendText("[" + Instant.now().toEpochMilli() + "] Hello " + message);
    }

    // 连接打开
    @OnOpen
    public void onOpen(Session session, @PathParam("id") Long id, EndpointConfig endpointConfig) {
        // 保存 session 到对象
        this.session = session;
        LOGGER.info("[websocket] 新的连接：id={}", this.session.getId());
        // 获取参数
        //Map<String, String> query = session.getPathParameters();
        Map<String, List<String>> query = session.getRequestParameterMap();
        String podName = query.get("podName").get(0);
        String nameSpace = query.get("nameSpace").get(0);
        Pod pod = K8sUtil.createKClient().pods().inNamespace(nameSpace).withName(podName).get();
        LogWatch logWatch = K8sUtil.createKClient().pods().inNamespace(nameSpace).withName(podName).watchLog(System.out);
        try {
            this.session.getBasicRemote().sendText("发送的消息是：" + pod.getMetadata().getName(),true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ThreadUtil.sleep(60 * 1000);
    }

    // 连接关闭
    @OnClose
    public void onClose(CloseReason closeReason) {
        LOGGER.info("[websocket] 连接断开：id={}，reason={}", this.session.getId(), closeReason);
    }

    // 连接异常
    @OnError
    public void onError(Throwable throwable) throws IOException {
        LOGGER.info("[websocket] 连接异常：id={}，throwable={}", this.session.getId(), throwable.getMessage());
        // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }
}
