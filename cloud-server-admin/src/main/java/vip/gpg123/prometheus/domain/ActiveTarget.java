package vip.gpg123.prometheus.domain;

import cn.hutool.json.JSONObject;
import lombok.Data;

@Data
public class ActiveTarget {

    private JSONObject discoveredLabels;

    private String globalUrl;

    private String health;

    private JSONObject labels;

    private String lastError;

    private String lastScrape;

    private Double lastScrapeDuration;

    private String scrapeInterval;

    private String scrapePool;

    private String scrapeTimeout;

    private String scrapeUrl;

}

