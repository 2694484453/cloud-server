package vip.gpg123.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.service.MineAppService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/app")
@Api(tags = "应用统计")
public class AppOverViewController extends BaseController {

    @Autowired
    private MineAppService mineAppService;

    /**
     * 应用概览
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "应用概览")
    public AjaxResult overView() {
        Map<String,Object> map = new LinkedHashMap<>();
        // 应用总数量
        map.put("appTotalCount", mineAppService.count(new LambdaQueryWrapper<MineApp>()
                .eq(MineApp::getCreateBy,  getUsername())
        ));
        return AjaxResult.success(map);
    }

}
