import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.app.domain.HelmAppMarket;
import vip.gpg123.app.domain.IndexResponse;
import vip.gpg123.app.service.HelmAppMarketService;
import vip.gpg123.common.utils.DateUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootTest(classes = {CloudServerApplication.class})
@RunWith(SpringRunner.class)
public class HelmInsertTest {

    @Autowired
    private HelmAppMarketService helmAppMarketService;

    @Test
    public void insert() {
        String icon = "https://dev-gpg.oss-cn-hangzhou.aliyuncs.com/icon/helm.jpg";
        HttpResponse response = HttpUtil.createRequest(Method.GET,"https://dev-gpg.oss-cn-hangzhou.aliyuncs.com/helm-charts/index.yaml")
                .header("Content-Type","application/x-www-form-urlencoded")
                .execute();
        String body = response.body();
        //
        InputStream inputStream = IoUtil.toStream(body, StandardCharsets.UTF_8);
        Map<String,Object> obj = YamlUtil.load(inputStream,Map.class);
        JSONObject jsonObject = JSONUtil.parseObj(obj);
        IndexResponse indexResponse = JSONUtil.toBean(jsonObject, IndexResponse.class);
        indexResponse.getEntries().forEach((k,v)->{
            System.out.println("key:"+k);
            v.forEach(entry->{
                //
                HelmAppMarket search = helmAppMarketService.getOne(new LambdaQueryWrapper<HelmAppMarket>()
                        .eq(StrUtil.isNotBlank(k),HelmAppMarket::getName,k)
                );
                if (search == null) {
                    // 添加
                    HelmAppMarket helmAppMarket = new HelmAppMarket();
                    helmAppMarket.setName(k);
                    helmAppMarket.setCreateBy("admin");
                    BeanUtils.copyProperties(entry,helmAppMarket);
                    if (StrUtil.isBlankIfStr(entry.getIcon())) {
                        helmAppMarket.setIcon(icon);
                    }
                    helmAppMarketService.save(helmAppMarket);
                }else  {
                    // 更新
                    search.setUpdateTime(DateUtils.getNowDate());
                    search.setUpdateBy("admin");
                    if (StrUtil.isBlankIfStr(search.getIcon())) {
                        search.setIcon(icon);
                    }
                    helmAppMarketService.updateById(search);
                    System.out.println(search.getName()+"已存在跳过");
                }
            });
        });
    }
}
