package vip.gpg123.domain;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.domain.service.DomainRecordService;

@RestController
@RequestMapping("/appDomain")
@Api(tags = "域名管理")
public class DomainRecordController extends BaseController {

    @Autowired
    private DomainRecordService appDomainService;
}
