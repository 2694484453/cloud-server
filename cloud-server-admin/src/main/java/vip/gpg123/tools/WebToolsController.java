package vip.gpg123.tools;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.tools.domain.WebTools;
import vip.gpg123.tools.service.WebToolsService;

import java.util.List;

@RestController
@RequestMapping("/tools")
@Api(tags = "工具")
public class WebToolsController {

    @Autowired
    private WebToolsService webToolsService;

    /**
     * 列表查询
     * @param type 类型
     * @param name 标题
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(@RequestParam(value = "type",required = false) String type,
                           @RequestParam(value = "name", required = false) String name) {
        List<WebTools> list = webToolsService.list(new LambdaQueryWrapper<WebTools>()
                .like(StrUtil.isNotBlank(name), WebTools::getName, name)
                .orderByDesc(WebTools::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param type 类型
     * @param name 标题
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page(@RequestParam(value = "type",required = false) String type,
                              @RequestParam(value = "name", required = false) String name) {
        IPage<WebTools> page = webToolsService.page(new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize()), new LambdaQueryWrapper<WebTools>()
                .like(StrUtil.isNotBlank(name), WebTools::getName, name)
                .orderByDesc(WebTools::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }
}
