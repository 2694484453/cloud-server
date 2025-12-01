package vip.gpg123.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vip.gpg123.traefik.domain.TraefikRouter;
import vip.gpg123.traefik.domain.TraefikService;

import java.util.List;

@FeignClient(name = "traefik-api", url = "${traefik.api:https://traefik.ecs.gpg123.vip/api}")
@Service
public interface TraefikApiService {

    /**
     * 查询router
     * @return r
     */
    @GetMapping("/http/routers")
    List<TraefikRouter> routers(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "per_page", required = false, defaultValue = "1000") int perPage);


    /**
     * 根据名称查询service
     * @param name 名称
     * @return r
     */
    @GetMapping("/http/services/{name}")
    TraefikService service(@PathVariable(value = "name") String name);

}
