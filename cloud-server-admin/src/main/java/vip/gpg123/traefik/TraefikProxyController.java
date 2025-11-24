package vip.gpg123.traefik;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.traefik.domain.TraefikProxy;
import vip.gpg123.traefik.mapper.TraefikProxyMapper;
import vip.gpg123.traefik.service.TraefikProxyService;

import java.util.List;

@RestController
@RequestMapping("/traefik")
public class TraefikProxyController extends BaseController {

    @Autowired
    private TraefikProxyMapper traefikProxyMapper;

    @Autowired
    private TraefikProxyService traefikProxyService;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name",required = false) String name,
                           @RequestParam(value = "type",required = false) String type) {
        List<TraefikProxy> list = traefikProxyService.list(new LambdaQueryWrapper<TraefikProxy>()
                .eq(TraefikProxy::getCreateBy,  getUserId())
                .like(StrUtil.isNotBlank(name),TraefikProxy::getName, name)
                .eq(StrUtil.isNotBlank(type), TraefikProxy::getType, type)
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
    public TableDataInfo page(@RequestParam(value = "name",required = false) String name,
                              @RequestParam(value = "type",required = false) String type) {

        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<TraefikProxy> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        TraefikProxy search = new TraefikProxy();
        search.setName(name);
        search.setType(type);
        search.setCreateBy(String.valueOf(getUserId()));
        List<TraefikProxy> list = traefikProxyMapper.page(pageDomain, search);
        page.setRecords(list);
        page.setTotal(traefikProxyMapper.list(search).size());
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 详情查询
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam(value = "id",required = false) String id) {
        TraefikProxy traefikProxy = traefikProxyService.getById(id);
        return AjaxResult.success(traefikProxy);
    }

    /**
     * 新增
     * @param traefikProxy t
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody TraefikProxy traefikProxy) {
        traefikProxy.setCreateBy(String.valueOf(getUserId()));
        traefikProxy.setCreateTime(DateUtil.date());
        // 对密码特殊处理
        boolean save = traefikProxyService.save(traefikProxy);
        return save ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     * @param traefikProxy t
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody TraefikProxy traefikProxy) {
        traefikProxy.setUpdateBy(String.valueOf(getUserId()));
        traefikProxy.setUpdateTime(DateUtil.date());
        boolean update = traefikProxyService.updateById(traefikProxy);
        return update ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam(value = "id",required = false) String id) {
        boolean remove = traefikProxyService.removeById(id);
        if (remove) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

}
