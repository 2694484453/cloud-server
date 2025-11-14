package vip.gpg123.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.repo.service.HelmRepoService;

@RestController
@RequestMapping("/app/common")
public class AppCommonController extends BaseController {

    @Autowired
    private HelmRepoService helmRepoService;


}
