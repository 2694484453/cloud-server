package vip.gpg123.app.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Entry {
    private String apiVersion;

    private String created;

    private String digest;

    private String icon;

    private List<String> urls;

    private String version;

    private String name;

    private String description;

    private String type;

    private String home;

    private List<Map<String,String>> maintainers;

    private List<String> sources;
}
