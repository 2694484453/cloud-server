import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.traefik.domain.TraefikLoadBalancer;
import vip.gpg123.traefik.domain.TraefikProxy;
import vip.gpg123.traefik.domain.TraefikRouter;
import vip.gpg123.traefik.domain.TraefikService;
import vip.gpg123.traefik.service.TraefikApiService;
import vip.gpg123.traefik.service.TraefikProxyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class TraefikTest {

    @Autowired
    private TraefikApiService traefikApiService;

    @Autowired
    private TraefikProxyService traefikProxyService;

    @Test
    public void insert() {
        List<TraefikRouter> list = traefikApiService.routers(1, 1000);
        list.forEach(traefikRouter -> {
            TraefikProxy proxy = new TraefikProxy();
            String name = traefikRouter.getName().substring(0, StrUtil.indexOf(traefikRouter.getName(), '@')).replaceAll("-router","");
            long count = traefikProxyService.count(new LambdaQueryWrapper<TraefikProxy>()
                    .eq(TraefikProxy::getName, name)
            );
            List<String> urls = new ArrayList<>();
            //
            if (traefikRouter.getService() != null && !traefikRouter.getService().contains("@")) {
                TraefikService traefikService = traefikApiService.service(traefikRouter.getService() + "@" + traefikRouter.getProvider());
                TraefikLoadBalancer loadBalancer = traefikService.getTraefikLoadBalancer();
                if (loadBalancer != null) {
                    JSONArray jsonArray = loadBalancer.getServers();
                    if (jsonArray != null && !jsonArray.isEmpty()) {
                        jsonArray.forEach(item -> {
                            JSONObject jsonObject = (JSONObject) JSONUtil.parse(item);
                            urls.add(jsonObject.get("url").toString());
                        });
                    }
                }
            }
            proxy.setServerUrls(String.join(",", urls));
            if (count == 1) {
                proxy.setStatus(traefikRouter.getStatus());
                proxy.setObservability(JSONUtil.toJsonStr(traefikRouter.getObservability()));
                proxy = traefikProxyService.getOne(new LambdaQueryWrapper<TraefikProxy>()
                        .eq(TraefikProxy::getName, name)
                );
                traefikProxyService.updateById(proxy);
            } else if (count == 0) {
                proxy.setName(name);
                proxy.setCreateBy("1");
                proxy.setCreateTime(DateUtil.date());
                proxy.setType("http");
                proxy.setEntryPoints(StrUtil.join(",", traefikRouter.getEntryPoints()));
                proxy.setDomains("");
                proxy.setStatus(traefikRouter.getStatus());
                proxy.setProvider(traefikRouter.getProvider());
                proxy.setDescription("");
                if (traefikRouter.getObservability() != null) {
                    List<String> tags = new ArrayList<>();
                    Map<String,Object> map = BeanUtil.beanToMap(traefikRouter.getObservability());
                    map.forEach((k,v)->{
                        String str = k + ":" + v.toString();
                        tags.add(str);
                    });
                    proxy.setTags(StrUtil.join(",", tags));
                }

                if (traefikRouter.getTls() != null) {
                    proxy.setTlsName(traefikRouter.getTls().getCertResolver());
                }
                System.out.println(proxy);
                traefikProxyService.save(proxy);
            }
        });
    }
}
