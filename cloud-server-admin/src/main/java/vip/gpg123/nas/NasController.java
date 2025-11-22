package vip.gpg123.nas;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.FrpServerApiService;
import vip.gpg123.nas.service.NasFrpClientService;
import vip.gpg123.nas.service.NasFrpServerService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
