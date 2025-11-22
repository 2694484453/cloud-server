package vip.gpg123.nas;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.nas.service.NasFrpClientService;
import vip.gpg123.nas.service.NasFrpServerService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/nas/frp/common")
@Api(value = "nas-frp通用管理")
@Slf4j
public class NasFrpCommonController extends BaseController {

    @Autowired
    private NasFrpClientService nasFrpClientService;

    @Autowired
    private NasFrpServerService nasFrpServerService;


    /**
     * 类型查询
     *
     * @return r
     */
    @GetMapping("/types")
    @ApiOperation(value = "【类型查询】")
    public AjaxResult types() {
        List<String> types = new ArrayList<>();
        types.add("http");
//        types.add("https");
//        types.add("tcp");
//        types.add("udp");
//        types.add("stcp");
//        types.add("sudp");
//        types.add("tcpmux");
        return AjaxResult.success(types);
    }

}
