package vip.gpg123.caddy;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import vip.gpg123.caddy.domain.CloudCaddy;
import vip.gpg123.caddy.mapper.CloudCaddyMapper;
import vip.gpg123.caddy.service.CaddyApi;
import vip.gpg123.caddy.service.CloudCaddyService;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.prometheus.domain.PrometheusExporter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:11
 **/
@RestController
@RequestMapping("/caddy")
@Api(tags = "caddy管理")
public class CaddyController extends BaseController {

    @Value("${basePath}")
    private String basePath;

    @Autowired
    private CaddyApi caddyApi;

    @Autowired
    private CloudCaddyService cloudCaddyService;

    @Autowired
    private CloudCaddyMapper cloudCaddyMapper;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(name = "name", required = false) String name) {
        List<CloudCaddy> list = cloudCaddyService.list(new LambdaQueryWrapper<CloudCaddy>()
                .eq(StrUtil.isNotBlank(name), CloudCaddy::getName, name)
                .eq(CloudCaddy::getCreateBy, getUserId())
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(name = "name", required = false) String name) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<CloudCaddy> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        CloudCaddy cloudCaddy = new CloudCaddy();
        cloudCaddy.setName(name);
        cloudCaddy.setCreateBy(String.valueOf(getUserId()));
        List<CloudCaddy> list = cloudCaddyMapper.page(pageDomain, cloudCaddy);
        page.setRecords(list);
        page.setTotal(cloudCaddyMapper.list(cloudCaddy).size());
        return PageUtils.toPage(list);
    }

    /**
     * 新增
     * @param cloudCaddy c
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "")
    public AjaxResult add(@RequestBody CloudCaddy cloudCaddy) {
        //
        boolean save = cloudCaddyService.save(cloudCaddy);
        return save ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }
}
