package vip.gpg123.amqp.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.amqp.producer.NasProducer;
import vip.gpg123.nas.domain.FrpServerHttp;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.FrpServerApi;
import vip.gpg123.nas.service.NasFrpClientService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NasConsumer {

    @Autowired
    private FrpServerApi frpServerApi;

    @Autowired
    private NasFrpClientService nasFrpClientService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = NasProducer.nasQueue, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = NasProducer.nasExchange), // 声明直接交换器
            key = "syncStatus" // 定义路由键
    ))
    public void syncStatus(){
        // 查询http代理
        String o = frpServerApi.test();
        List<FrpServerHttp> httpList = frpServerApi.httpList().getProxies();
        Map<String, FrpServerHttp> httpMap = httpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 更新

        // 查询https代理
        List<FrpServerHttp> httpsList = frpServerApi.httpsList().getProxies();
        Map<String, FrpServerHttp> httpsMap = httpsList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询tcp代理
        List<FrpServerHttp> tcpList = frpServerApi.tcpList().getProxies();
        Map<String, FrpServerHttp> tcpMap = tcpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询udp代理
        List<FrpServerHttp> udpList = frpServerApi.udpList().getProxies();
        Map<String, FrpServerHttp> udpMap = udpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 获取list
        List<NasFrpClient> list = nasFrpClientService.list();
        list.forEach(item -> {
            switch (item.getType()) {
                case "http":
                    if (httpMap.containsKey(item.getName())) {
                        item.setStatus(httpMap.get(item.getName()).getStatus());
                    } else {
                        item.setStatus("noExist");
                    }
                    break;
                case "https":
                    if (httpsMap.containsKey(item.getName())) {
                        item.setStatus(httpsMap.get(item.getName()).getStatus());
                    }else {
                        item.setStatus("noExist");
                    }
                    break;
                case "tcp":
                    if (tcpMap.containsKey(item.getName())) {
                        item.setStatus(tcpMap.get(item.getName()).getStatus());
                    }else {
                        item.setStatus("noExist");
                    }
                    break;
                case "udp":
                    if (udpMap.containsKey(item.getName())) {
                        item.setStatus(udpMap.get(item.getName()).getStatus());
                    }else {
                        item.setStatus("noExist");
                    }
                    break;
                default:
                    break;
            }
            nasFrpClientService.updateById(item);
        });
    }
}
