package vip.gpg123.nas;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.nas.service.FrpServerApiService;
import vip.gpg123.nas.service.NasFrpClientService;
import vip.gpg123.nas.service.NasFrpServerService;

/**
 * nasOverView控制
 */
@RestController
@RequestMapping("/nas")
@Api(tags = "nasOverView控制")
public class NasController extends BaseController {

    @Autowired
    private NasFrpServerService nasFrpServerService;

    @Autowired
    private NasFrpClientService nasFrpClientService;

    @Autowired
    private FrpServerApiService frpServerApiService;


}
