package vip.gpg123.app;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.HelmAppMarket;
import vip.gpg123.app.domain.IndexResponse;
import vip.gpg123.app.domain.MineApp;
import vip.gpg123.app.service.HelmAppMarketService;
import vip.gpg123.app.service.MineAppService;
import vip.gpg123.app.vo.HelmAppMarketVo;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.DateUtils;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.common.utils.helm.HelmUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/market")
@Api(value = "应用市场")
public class AppMarketController extends BaseController {

    @Value("${repo.helm.url}")
    private String helmUrl;

    @Autowired
    private HelmAppMarketService helmAppMarketService;

    @Autowired
    private AppManagerController appManagerController;

    @Autowired
    private MineAppService mineAppService;


    /**
     * 列表查询
     *
     * @param name   名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "version", required = false) String version,
                           @RequestParam(value = "status", required = false) String status) {
        // 查询
        List<HelmAppMarket> helmAppMarkets = helmAppMarketService.list(new LambdaQueryWrapper<HelmAppMarket>()
                        .like(StrUtil.isNotBlank(name), HelmAppMarket::getName, name)
                        .like(StrUtil.isNotBlank(version), HelmAppMarket::getVersion, version)
                        .eq(StrUtil.isNotBlank(status), HelmAppMarket::getStatus, status)
                //.eq(HelmAppMarket::getCreateBy, getUsername())
        );
        List<HelmAppMarket> res = new ArrayList<>();
        helmAppMarkets.forEach(helmAppMarket -> {
            MineApp mineApp = mineAppService.getOne(new LambdaQueryWrapper<MineApp>().eq(MineApp::getAppName, helmAppMarket.getName()));
            if (mineApp != null) {
                helmAppMarket.setStatus("installed");
            }
            res.add(helmAppMarket);
        });
        return AjaxResult.success(res);
    }

    /**
     * 分页查询
     *
     * @param page   page
     * @param name   名称
     * @param status 状态
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(Page<HelmAppMarket> page,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "version", required = false) String version,
                              @RequestParam(value = "status", required = false) String status) {
        // 获取分页参数
        PageUtils.toIPage(page);
        IPage<HelmAppMarket> pageRes = helmAppMarketService.page(page, new LambdaQueryWrapper<HelmAppMarket>()
                        .like(StrUtil.isNotBlank(name), HelmAppMarket::getName, name)
                        .like(StrUtil.isNotBlank(version), HelmAppMarket::getVersion, version)
                        .eq(StrUtil.isNotBlank(status), HelmAppMarket::getStatus, status)
                //.eq(HelmAppMarket::getCreateBy, getUsername())
        );
        return PageUtils.toPageByIPage(pageRes);
    }

    /**
     * 删除
     *
     * @param helmAppMarket h
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestBody HelmAppMarket helmAppMarket) {
        boolean del = helmAppMarketService.removeById(helmAppMarket.getId());
        return del ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 同步数据
     *
     * @return r
     */
    @PostMapping("/sync")
    @ApiOperation(value = "同步")
    public AjaxResult sync() {
        String icon = "https://dev-gpg.oss-cn-hangzhou.aliyuncs.com/icon/helm.jpg";
        HttpResponse response = HttpUtil.createRequest(Method.GET, helmUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .execute();
        String body = response.body();
        //
        InputStream inputStream = IoUtil.toStream(body, StandardCharsets.UTF_8);
        Map<String, Object> obj = YamlUtil.load(inputStream, Map.class);
        JSONObject jsonObject = JSONUtil.parseObj(obj);
        IndexResponse indexResponse = JSONUtil.toBean(jsonObject, IndexResponse.class);
        indexResponse.getEntries().forEach((k, v) -> {
            System.out.println("key:" + k);
            v.forEach(entry -> {
                HelmAppMarket helmAppMarket = new HelmAppMarket();
                //
                HelmAppMarket search = helmAppMarketService.getOne(new LambdaQueryWrapper<HelmAppMarket>()
                        .eq(StrUtil.isNotBlank(k), HelmAppMarket::getName, k)
                );
                if (search == null) {
                    // 添加
                    helmAppMarket.setName(k);
                    helmAppMarket.setCreateBy("admin");
                    BeanUtils.copyProperties(entry, helmAppMarket);
                    if (StrUtil.isBlankIfStr(entry.getIcon())) {
                        helmAppMarket.setIcon(icon);
                    }
                    helmAppMarket.setUrl(helmUrl + "/" + entry.getUrls().get(0));
                    helmAppMarketService.save(helmAppMarket);
                } else {
                    // 更新
                    helmAppMarket = search;
                    helmAppMarket.setUpdateTime(DateUtils.getNowDate());
                    helmAppMarket.setUpdateBy("admin");
                    if (StrUtil.isBlankIfStr(helmAppMarket.getIcon())) {
                        helmAppMarket.setIcon(icon);
                    }
                    helmAppMarketService.updateById(helmAppMarket);
                    System.out.println(helmAppMarket.getName() + "已存在跳过");
                }
            });
        });
        return AjaxResult.success("同步完成");
    }


    /**
     * values
     *
     * @param helmAppMarket mark
     * @return r
     */
    @PostMapping("/values")
    @ApiOperation(value = "渲染参数")
    public AjaxResult values(@RequestBody HelmAppMarket helmAppMarket) {
        String values = HelmUtils.showValues(helmAppMarket.getUrl());

        JSONObject jsonObject = new JSONObject();
        try {
            InputStream inputStream = IoUtil.toStream(values, StandardCharsets.UTF_8);
            Map<String,Object> obj = YamlUtil.load(inputStream, Map.class);
            jsonObject = JSONUtil.parseObj(obj);
        } catch (Exception e) {
            throw new RuntimeException("不能转换为yaml格式："+e);
        }
        return AjaxResult.success(jsonObject);
    }

    /**
     * 安装
     *
     * @return r
     */
    @PostMapping("/install")
    @ApiOperation(value = "安装")
    public AjaxResult install(@RequestBody HelmAppMarketVo helmAppMarket) {
        MineApp mineApp = new MineApp();
        mineApp.setAppName(helmAppMarket.getName());
        mineApp.setReleaseName(helmAppMarket.getName());
        mineApp.setChartName(helmAppMarket.getName());
        mineApp.setDescription(helmAppMarket.getDescription());
        mineApp.setCreateBy(SecurityUtils.getUsername());
        mineApp.setCreateTime(DateUtils.getNowDate());
        mineApp.setNameSpace(SecurityUtils.getUserId().toString());
        mineApp.setIcon(helmAppMarket.getIcon());
        mineApp.setValue(String.valueOf(helmAppMarket.getValues()));
        mineApp.setChartUrl(helmAppMarket.getUrl());
        return appManagerController.install(mineApp);
    }
}
