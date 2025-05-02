package vip.gpg123.framework.config;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.gpg123.framework.config.domain.NacosClient;

/**
 * @author gaopuguang
 * @date 2025/3/2 15:49
 **/
@Configuration
public class NacosConfig {

    private static final String api = "http://hcs.gpg123.vip:8848";

    private static final String username = "";

    private static final String password = "";

    @Bean
    public NacosClient nacosClient() {
        return new NacosClient(api, username, password);
    }

    /**
     * 创建命名空间
     * @param nameSpaceId id
     * @param nameSpaceName name
     * @param nameSpaceDesc desc
     * @return r
     */
    public static Boolean createNs(String nameSpaceId, String nameSpaceName, String nameSpaceDesc) {
        HttpResponse httpResponse = HttpRequest.post(api + "/nacos/v1/console/namespaces")
                .form("customNamespaceId", nameSpaceId)
                .form("namespaceName", nameSpaceName)
                .form("namespaceDesc", nameSpaceDesc)
                .execute();
        return Boolean.valueOf(httpResponse.body());
    }
}
