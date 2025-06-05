package vip.gpg123.domain.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class AliYunDomainRecordResponse implements Serializable {

    private String RequestId;

    private String RecordId;
}
