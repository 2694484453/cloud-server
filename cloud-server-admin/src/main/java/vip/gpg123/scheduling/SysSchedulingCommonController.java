package vip.gpg123.scheduling;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scheduling/common")
@Api(tags = "调度中心common")
public class SysSchedulingCommonController extends BaseController {


    /**
     * 类型查询
     * @return r
     */
    @GetMapping("/types")
    @ApiOperation(value = "类型")
    public AjaxResult types() {
        List<Map<String,String>> types = new ArrayList<>();
        // 远程主机
        types.add(new HashMap<String,String>(){{
            put("value","RemoteShell");
            put("label","执行远程主机命令");
        }});
        types.add(new HashMap<String,String>(){{
            put("value","api");
            put("label","接口请求");
        }});
        return AjaxResult.success(types);
    }

}
