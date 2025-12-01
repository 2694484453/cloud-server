package vip.gpg123.domain;

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
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.domain.domain.CloudDomain;
import vip.gpg123.domain.mapper.CloudDomainMapper;
import vip.gpg123.domain.service.CloudDomainService;

import java.util.List;

@RestController
@RequestMapping("/domain")
@Api(tags = "域名管理")
public class CloudDomainController extends BaseController {

    @Autowired
    private CloudDomainService cloudDomainService;

    @Autowired
    private CloudDomainMapper cloudDomainMapper;

    /**
     * 列表查询
     *
     * @param name 名称
     * @param type 类型
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "type", required = false) String type) {
        List<CloudDomain> list = cloudDomainService.list(new LambdaQueryWrapper<CloudDomain>()
                .eq(StrUtil.isNotBlank(type), CloudDomain::getType, type)
                .like(StrUtil.isNotBlank(name), CloudDomain::getDomainName, name)
                .eq(CloudDomain::getCreateBy, getUserId())
                .orderByDesc(CloudDomain::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param name 名称
     * @param type 类型
     * @return r
     */
    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo repos(@RequestParam(value = "domainName", required = false) String domainName,
                               @RequestParam(value = "type", required = false) String type) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<CloudDomain> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        // 获取token
        CloudDomain cloudDomain = new CloudDomain();
        cloudDomain.setType(type);
        cloudDomain.setDomainName(domainName);
        cloudDomain.setCreateBy(String.valueOf(getUserId()));

        // 查询
        List<CloudDomain> list = cloudDomainMapper.page(pageDomain, cloudDomain);
        page.setRecords(list);
        page.setTotal(cloudDomainMapper.list(cloudDomain).size());
        return PageUtils.toPage(list);
    }
}
