package vip.gpg123.domain.service;

import com.aliyun.sdk.service.alidns20150109.models.AddDomainRecordResponseBody;

public interface AliYunDomainApi {


    /**
     * 新增域名解析
     * @param domainName domainName
     * @param type type
     * @param Rr rr
     * @param value v
     * @return r
     */
    AddDomainRecordResponseBody addDomainRecord(String domainName, String type, String Rr, String value);
}
