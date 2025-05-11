package vip.gpg123.nas;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/nas/frp/common")
@Api(value = "nas-frp通用管理")
public class NasFrpCommonController extends BaseController {


    /**
     * 类型查询
     * @return r
     */
    @GetMapping("/types")
    @ApiOperation(value = "【类型查询】")
    public AjaxResult types() {
        List<String> types = new ArrayList<>();
        types.add("http");
        types.add("https");
        types.add("tcp");
        types.add("udp");
        types.add("stcp");
        types.add("sudp");
        types.add("tcpmux");
        return AjaxResult.success(types);
    }
}
