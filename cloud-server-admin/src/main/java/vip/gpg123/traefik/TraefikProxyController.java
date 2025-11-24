package vip.gpg123.traefik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.traefik.mapper.TraefikProxyMapper;
import vip.gpg123.traefik.service.TraefikProxyService;

@RestController
@RequestMapping("/traefikProxy")
public class TraefikProxyController {

    @Autowired
    private TraefikProxyMapper traefikProxyMapper;

    @Autowired
    private TraefikProxyService traefikProxyService;


}
