package vip.gpg123.git.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FrpServerHttpResponse implements Serializable {

    List<FrpServerHttp> proxies;
}
