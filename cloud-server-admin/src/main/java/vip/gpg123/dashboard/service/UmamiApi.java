package vip.gpg123.dashboard.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vip.gpg123.dashboard.config.UmamiFeignConfig;
import vip.gpg123.dashboard.domain.Umami;
import vip.gpg123.dashboard.domain.UmamiStats;

@FeignClient(name = "umami-api", url = "${analytics.umami.api}", configuration = UmamiFeignConfig.class)
public interface UmamiApi {

    /**
     * 查询token
     *
     * @param shareId id
     * @return r
     */
    @GetMapping("/share/{shareId}")
    Umami share(@PathVariable("shareId") String shareId);

    /**
     * top📚
     *
     * @param startAt       sa
     * @param endAt         ea
     * @param unit          u
     * @param timezone      t
     * @return r
     */
    @GetMapping("/websites/{websiteId}/stats")
    UmamiStats stats(@PathVariable("websiteId") String websiteId,
                     @RequestParam(value = "startAt", required = false) String startAt,
                     @RequestParam(value = "endAt", required = false) String endAt,
                     @RequestParam(value = "unit", required = false) String unit,
                     @RequestParam(value = "timezone", required = false) String timezone);
}
