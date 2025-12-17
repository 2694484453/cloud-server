package vip.gpg123.web.controller.monitor;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsRedirectController {

    private final PrometheusMeterRegistry meterRegistry;

    public MetricsRedirectController(PrometheusMeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    // 将 /metrics 映射到 Prometheus 指标
    @GetMapping("/metrics")
    public String prometheusMetrics() {
        return meterRegistry.scrape();
    }
}