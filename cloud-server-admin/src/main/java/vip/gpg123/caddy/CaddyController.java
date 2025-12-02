package vip.gpg123.caddy;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.URLEncoder;
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
     *
     * @param cloudCaddy c
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    public AjaxResult add(@RequestBody CloudCaddy cloudCaddy) {
        // 查询
        int count = cloudCaddyService.count(new LambdaQueryWrapper<CloudCaddy>()
                .eq(CloudCaddy::getName, cloudCaddy.getName())
        );
        if (count > 0) {
            return AjaxResult.error("名称已被占用，请更换");
        }
        cloudCaddy.setCreateBy(String.valueOf(getUserId()));
        cloudCaddy.setCreateTime(DateUtil.date());
        boolean save = cloudCaddyService.save(cloudCaddy);
        return save ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }

    /**
     * 导出
     * @param response r
     */
    @GetMapping("/exporter")
    @ApiOperation(value = "【导出】")
    public void exporter(HttpServletResponse response) {
        JSONObject jsonObject = caddyApi.config();
        String jsonStr = JSONUtil.toJsonStr(jsonObject);
        File file = FileUtil.createTempFile();
        try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonStr);
            // 1. 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("caddy.json", "UTF-8"));
            // 2. 带缓冲的流复制
            IoUtil.copy(FileUtil.getInputStream(file), out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(file);
        }
    }
}
