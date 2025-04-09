package vip.gpg123.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cloud-server")
public interface TestFeign {

    @GetMapping("/test")
    void test();
}

