package vip.gpg123.app.domain;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class IndexResponse implements Serializable {

    private String apiVersion;

    private Map<String, List<Entry>> entries;

    private String generated;

}
