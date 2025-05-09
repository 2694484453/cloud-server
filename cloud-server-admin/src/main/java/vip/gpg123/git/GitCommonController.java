package vip.gpg123.git;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/git/common")
@Api(tags = "git通用查询")
public class GitCommonController {

    /**
     * 获取type
     *
     * @return r
     */
    @GetMapping("/types")
    @ApiOperation(value = "获取type类型")
    public AjaxResult token() {
        List<String> types = new ArrayList<>();
        types.add("gitee");
        types.add("github");
        types.add("gitlab");
        types.add("gitcode");
        return AjaxResult.success(types);
    }
}
