package vip.gpg123.app.domain;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@Getter
public class IndexResponse {

    private String apiVersion;

    private Map<String, List<Entry>> entries;

    private String generated;

}
