package vip.gpg123.domain.service.impl;

import com.aliyun.sdk.service.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.sdk.service.alidns20150109.models.AddDomainRecordResponse;
import com.aliyun.sdk.service.alidns20150109.models.AddDomainRecordResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.domain.service.AliYunDomainApi;
import vip.gpg123.framework.client.AsyncDomainClient;

import java.util.concurrent.CompletableFuture;

@Service
public class AliYunDomainApiServiceImpl implements AliYunDomainApi {

    @Autowired
    private AsyncDomainClient asyncDomainClient;

    /**
     * 新增域名解析
     *
     * @param domainName domainName
     * @param type       type
     * @param Rr         rr
     * @param value      v
     * @return r
     */
    @Override
    public AddDomainRecordResponseBody addDomainRecord(String domainName, String type, String Rr, String value) {
        AddDomainRecordRequest addDomainRecordRequest = AddDomainRecordRequest.builder()
                .domainName(domainName)
                .rr(Rr)
                .type(type)
                .value(value)
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();
        CompletableFuture<AddDomainRecordResponse> response = asyncDomainClient.addDomainRecord(addDomainRecordRequest);
        try {
            AddDomainRecordResponse resp = response.get();
            return resp.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
