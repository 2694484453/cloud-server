package com.ruoyi.framework.config;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.ruoyi.common.utils.K8sUtil;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.dsl.LogWatch;
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
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 使用 @ServerEndpoint 注解表示此类是一个 WebSocket 端点
// 通过 value 注解，指定 websocket 的路径
@ServerEndpoint(value = "/channel/podLogChannel/{id}")
@Component
public class PodLogWebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PodLogWebSocketServer.class);

    private static final Map<String, Session> sessionMap = Collections.synchronizedMap(new HashMap<>());

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

    @OnMessage
    public void onBinaryMessage(ByteBuffer message, Session session) {
        System.out.println("Received binary message");
        try {
            session.getBasicRemote().sendBinary(message);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // 连接打开
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        // 保存 session 到对象
        this.session = session;
        sessionMap.put(id, session);
        LOGGER.info("[websocket] 新的连接：id={}", id);
        System.out.println("[websocket] Session ID: " + session.getId());
    }

    // 连接关闭
    @OnClose
    public void onClose(Session session, CloseReason closeReason, @PathParam("id") String id) {
        sessionMap.remove(id);
        LOGGER.info("[websocket] 连接断开：id={}，reason={}", this.session.getId(), closeReason);
    }

    // 连接异常
    @OnError
    public void onError(Session session, @PathParam("id") String id, Throwable throwable) throws IOException {
        LOGGER.info("[websocket] 连接异常：id={}，throwable={}", id, throwable.getMessage());
        // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }

    /**
     * 发送消息
     *
     * @param id      id
     * @param message 消息
     */
    public static void sendMessageToClient(String id, String message) {
        Session session = sessionMap.get(id);
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
     *
     * @param id id
     * @param is 消息
     */
    public static void sendMessageSteamToClient(String id, InputStream is) {
        Session session = sessionMap.get(id);
        if (session != null && session.isOpen()) {
            try {
                OutputStream os = session.getBasicRemote().getSendStream();
                IoUtil.copy(is, os);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
