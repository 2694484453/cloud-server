package vip.gpg123.app;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;

/**
 * 我的应用
 */
@RestController
@RequestMapping("/helm/app")
@Api(value = "我的应用")
public class MineAppController {

    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        return AjaxResult.success();
    }

}
