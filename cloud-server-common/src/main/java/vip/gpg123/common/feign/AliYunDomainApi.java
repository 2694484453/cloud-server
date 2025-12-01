package vip.gpg123.common.feign;

import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.AddDomainRecordResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "aliyun-domain-api", url = "https://alidns.cn-hangzhou.aliyuncs.com")
@Service
public interface AliYunDomainApi {


    /**
     * 新增域名解析
     * @param request 请求
     * @return r
     */
    @PostMapping("/")
    AddDomainRecordResponseBody addDomainRecord(@RequestBody AddDomainRecordRequest request);

}
