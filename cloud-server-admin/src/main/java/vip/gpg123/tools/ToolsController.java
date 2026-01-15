package vip.gpg123.tools;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.tools.domain.WebTools;
import vip.gpg123.tools.service.WebToolsService;

import java.util.List;

@RestController
@RequestMapping("/tools")
@Api(tags = "工具")
public class ToolsController {

    @Autowired
    private WebToolsService webToolsService;

    /**
     * 列表查询
     * @param webTools w
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(WebTools webTools) {
        List<WebTools> list = webToolsService.list(webTools);
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param page p
     * @param webTools w
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(Page<WebTools> page, WebTools webTools) {
        IPage<WebTools> pageRes = webToolsService.page(page, webTools);
        return PageUtils.toPageByIPage(pageRes);
    }
}
