package com.ruoyi.framework.config;

import cn.hutool.core.thread.ThreadUtil;
import com.ruoyi.common.utils.K8sUtil;
import io.fabric8.kubernetes.client.dsl.PodResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/ws/podLog")
@Component
public class PodLogWebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PodLogWebSocketServer.class);

    private static final Map<String, Session> sessionMap = Collections.synchronizedMap(new HashMap<>());

    private Session session;

    // 收到消息
    @OnMessage
    public void onMessage(String message) {
        LOGGER.info("[websocket] 收到消息：id={}，message={}", this.session.getId(), message);
    }

    // 连接打开
    @OnOpen
    public void onOpen(Session session) {
        // 保存 session 到对象
        this.session = session;
        sessionMap.put("podLog", session);
        LOGGER.info("[websocket] 新的连接：id={}", "podLog");
        // 开始启动日志流
        String nameSpace = session.getRequestParameterMap().get("nameSpace").get(0);
        String podName = session.getRequestParameterMap().get("podName").get(0);
        sendMessageToClient(podName, nameSpace);
    }

    // 连接关闭
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessionMap.remove("podLog");
        LOGGER.info("[websocket] 连接断开：id={}，reason={}", this.session.getId(), closeReason);
    }

    // 连接异常
    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        LOGGER.info("[websocket] 连接异常：id={}，throwable={}", "podLog", throwable.getMessage());
        // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public static void sendMessageToClient(String message) {
        Session session = sessionMap.get("podLog");
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * 发送消息
     */
    public static void sendMessageToClient(String podName, String nameSpace) {
        Session session = sessionMap.get("podLog");
        if (session != null && session.isOpen()) {
            ThreadUtil.execute(() -> {
                try {
                    PodResource podResource = K8sUtil.createKClient().pods().inNamespace(nameSpace).withName(podName);
                    session.getBasicRemote().sendText(podResource.getLog());
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }
    }
}
