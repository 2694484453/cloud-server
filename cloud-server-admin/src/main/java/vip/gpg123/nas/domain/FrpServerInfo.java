package vip.gpg123.nas.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class FrpServerInfo implements Serializable {

    /**
     * 版本号，用于标识软件的当前版本
     */
    private String version;

    /**
     * 绑定端口，用于配置服务监听的端口号
     */
    private int bindPort;

    /**
     * 虚拟主机HTTP端口，用于处理HTTP请求的端口配置
     */
    private int vhostHTTPPort;

    /**
     * 虚拟主机HTTPS端口，用于处理HTTPS请求的端口配置
     */
    private int vhostHTTPSPort;

    /**
     * TCPMUX HTTP连接端口，用于特定HTTP连接方式的端口配置
     */
    private int tcpmuxHTTPConnectPort;

    /**
     * KCP协议绑定端口，用于KCP协议通信的端口配置
     */
    private int kcpBindPort;

    /**
     * QUIC协议绑定端口，用于QUIC协议通信的端口配置
     */
    private int quicBindPort;

    /**
     * 子域名主机名，用于配置和识别子域名
     */
    private String subdomainHost;

    /**
     * 最大线程池数量，用于限制处理请求的线程数量
     */
    private int maxPoolCount;

    /**
     * 每个客户端最大端口数量，用于限制每个客户端可以使用的端口数
     */
    private int maxPortsPerClient;

    /**
     * 心跳超时时间，用于维持连接活跃状态的定时检测配置
     */
    private int heartbeatTimeout;

    /**
     * 总输入流量，用于统计进入的流量数据
     */
    private int totalTrafficIn;

    /**
     * 总输出流量，用于统计输出的流量数据
     */
    private int totalTrafficOut;

    /**
     * 当前连接数，用于实时统计当前的连接数量
     */
    private int curConns;

    /**
     * 客户端数量，用于统计连接到服务的客户端数目
     */
    private int clientCounts;

    /**
     * 代理类型计数，用于统计不同类型的代理连接数量
     */
    private ProxyTypeCount proxyTypeCount;
}
