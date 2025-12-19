package vip.gpg123.app;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
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
import vip.gpg123.app.domain.HelmApp;
import vip.gpg123.app.service.HelmAppMarketService;
import vip.gpg123.app.service.HelmAppService;
import vip.gpg123.app.service.HelmRepoApi;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/market")
@Api(value = "应用市场")
public class HelmAppMarketController extends BaseController {

    @Autowired
    private HelmAppMarketService helmAppMarketService;

    @Autowired
    private HelmAppService helmAppService;

    @Autowired
    private HelmRepoApi helmRepoApi;

    private static final String icon = "https://dev-gpg.oss-cn-hangzhou.aliyuncs.com/icon/helm.jpg";

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
        );
        return AjaxResult.success(helmAppMarkets);
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
    @GetMapping("/sync")
    @ApiOperation(value = "同步")
    public AjaxResult sync() {
        String body = helmRepoApi.index();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        IndexResponse indexResponse = JSONUtil.toBean(jsonObject, IndexResponse.class);
        indexResponse.getEntries().forEach((k, v) -> {
            System.out.println("key:" + k);
            v.forEach(entry -> {
                //
                HelmAppMarket search = helmAppMarketService.getOne(new LambdaQueryWrapper<HelmAppMarket>()
                        .eq(StrUtil.isNotBlank(k), HelmAppMarket::getName, k)
                );
                if (search == null) {
                    HelmAppMarket helmAppMarket = new HelmAppMarket();
                    // 添加
                    helmAppMarket.setName(k);
                    helmAppMarket.setCreateBy("admin");
                    BeanUtils.copyProperties(entry, helmAppMarket);
                    if (StrUtil.isBlankIfStr(entry.getIcon())) {
                        helmAppMarket.setIcon(icon);
                    }
                    helmAppMarket.setUrl(entry.getUrls().get(0));
                    helmAppMarketService.save(helmAppMarket);
                } else {
                    boolean isUpdate = false;
                    // 更新
                    if (!entry.getUrls().get(0).equals(search.getUrl())) {
                        search.setUrl(entry.getUrls().get(0));
                        isUpdate = true;
                    }
                    if (StrUtil.isNotBlank(entry.getIcon()) && !entry.getIcon().equals(search.getIcon())) {
                        search.setIcon(entry.getIcon());
                        isUpdate = true;
                    }
                    if (StrUtil.isBlank(entry.getIcon())) {
                        search.setIcon(icon);
                        isUpdate = true;
                    }
                    if (isUpdate) {
                        search.setUpdateTime(DateUtils.getNowDate());
                        search.setUpdateBy("admin");
                        helmAppMarketService.updateById(search);
                    }
                    System.out.println(search.getName() + "已存在跳过");
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
            Map<String, Object> obj = YamlUtil.load(inputStream, Map.class);
            jsonObject = JSONUtil.parseObj(obj);
        } catch (Exception e) {
            throw new RuntimeException("不能转换为yaml格式：" + e);
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
        // 检查
        String releaseName = helmAppMarket.getReleaseName();
        String nameSpace = helmAppMarket.getNameSpace();
        long count = helmAppService.count(new LambdaQueryWrapper<HelmApp>()
                .eq(HelmApp::getReleaseName, releaseName)
                .eq(HelmApp::getNameSpace, nameSpace)
        );
        if (count > 0) {
            return AjaxResult.error("安装失败：" + nameSpace + "下已存在" + releaseName);
        }
        HelmApp helmApp = new HelmApp();
        helmApp.setAppName(helmAppMarket.getName());
        helmApp.setReleaseName(helmAppMarket.getName());
        helmApp.setChartName(helmAppMarket.getName());
        helmApp.setDescription(helmAppMarket.getDescription());
        helmApp.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        helmApp.setCreateTime(DateUtils.getNowDate());
        helmApp.setNameSpace(SecurityUtils.getUserId().toString());
        helmApp.setIcon(helmAppMarket.getIcon());
        helmApp.setChartValues(String.valueOf(helmAppMarket.getChartValues()));
        helmApp.setStatus("installing");
        helmApp.setChartUrl(helmAppMarket.getUrl());
        BeanUtils.copyProperties(helmAppMarket, helmApp);
        boolean result = helmAppService.save(helmApp);
        return result ? AjaxResult.success("安装成功") : AjaxResult.error("安装失败");
    }
}
