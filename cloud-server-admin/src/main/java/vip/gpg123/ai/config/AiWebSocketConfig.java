package vip.gpg123.ai.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2025/1/29 3:04
 **/
@ServerEndpoint(value = "/ws/ai/{id}")
@Component
public class AiWebSocketConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiWebSocketConfig.class);

    private static final Map<String, Session> sessionMap = Collections.synchronizedMap(new HashMap<>());

    private Session session;

    /**
     * 打开
     *
     * @param session s
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        // 保存 session 到对象
        this.session = session;
        sessionMap.put(id, session);
        LOGGER.info("[websocket] 新的连接：id={}", "podLog");
    }

    /**
     * 连接关闭
     *
     * @param session     s
     * @param closeReason c
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason, @PathParam("id") String id) {
        sessionMap.remove(id);
        LOGGER.info("[websocket] 连接断开：id={}，reason={}", this.session.getId(), closeReason);
    }

    /**
     * 异常
     *
     * @param session   s
     * @param throwable t
     * @throws IOException e
     */
    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        LOGGER.info("[websocket] 连接异常：id={}，throwable={}", "podLog", throwable.getMessage());
        // 关闭连接。状态码为 UNEXPECTED_CONDITION（意料之外的异常）
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }

    /**
     * 发送消息
     */
    public void sendMessageToClient(String id, String message) {
        Session session = sessionMap.get(id);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
