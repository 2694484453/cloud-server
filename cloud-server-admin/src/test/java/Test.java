import cn.hutool.core.date.DateUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.nas.NasFrpClientController;
import vip.gpg123.nas.domain.FrpServerHttp;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.FrpServerApiService;
import vip.gpg123.nas.service.NasFrpClientService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private FrpServerApiService frpServerApiService;

    @Autowired
    private NasFrpClientService nasFrpClientService;

    @org.junit.Test
    public void run() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        String beanName = "apiTask";
        String methodName = "syncNasFrpClientStatus";
        System.out.println(SpringUtils.containsBean(beanName));
        Object bean = SpringUtils.getBean(beanName);
        //无参数调用
        Method method = bean.getClass().getMethod(methodName);
        method.invoke(bean);
    }

    @org.junit.Test
    public void runWithArgs() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        String beanName = "apiTask";
        String methodName = "syncNasFrpClientStatus";
        Map<String, Object> params = new HashMap<>();
        params.put("id", "1");
        System.out.println(SpringUtils.containsBean(beanName));
        Object bean = SpringUtils.getBean(beanName);
        //有参数调用
        Method method = bean.getClass().getMethod(methodName, Map.class);
        method.invoke(bean, params);
    }

    @org.junit.Test
    public void syncNasFrpClientStatus() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        SpringUtils.getBean(NasFrpClientController.class).sync();
    }

    @org.junit.Test
    public void importConfig() {
        // 查询http代理
        List<FrpServerHttp> httpList = frpServerApiService.httpList().getProxies();
        Map<String, FrpServerHttp> httpMap = httpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 更新

        // 查询https代理
        List<FrpServerHttp> httpsList = frpServerApiService.httpsList().getProxies();
        Map<String, FrpServerHttp> httpsMap = httpsList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询tcp代理
        List<FrpServerHttp> tcpList = frpServerApiService.tcpList().getProxies();
        Map<String, FrpServerHttp> tcpMap = tcpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 查询udp代理
        List<FrpServerHttp> udpList = frpServerApiService.udpList().getProxies();
        Map<String, FrpServerHttp> udpMap = udpList.stream().collect(Collectors.toMap(FrpServerHttp::getName, Function.identity()));
        // 获取list
        List<NasFrpClient> list = nasFrpClientService.list();

        Map<String,NasFrpClient> map = list.stream().collect(Collectors.toMap(NasFrpClient::getName,Function.identity()));

        httpMap.forEach((key, value) -> {
            NasFrpClient nasFrpClient = new NasFrpClient();
            if (!map.containsKey(key)) {
                nasFrpClient.setType(value.getName());
                nasFrpClient.setCreateTime(DateUtil.date());
                nasFrpClient.setCreateBy("1");
                nasFrpClient.setName(value.getName());
                nasFrpClient.setLocalIp(String.valueOf(value.getConf().get("localIp")));
                nasFrpClient.setCustomDomains(String.valueOf(value.getConf().get("customDomain")));
                nasFrpClientService.save(nasFrpClient);
            }
        });
    }
}
