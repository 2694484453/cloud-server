package vip.gpg123.domain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
                .like(StrUtil.isNotBlank(name), CloudDomain::getDomain, name)
                .eq(CloudDomain::getCreateBy, getUserId())
                .orderByDesc(CloudDomain::getCreateTime)
        );
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param domain     名称
     * @param type       类型
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo repos(@RequestParam(value = "domain", required = false) String domain,
                               @RequestParam(value = "type", required = false) String type) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<CloudDomain> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        // 获取token
        CloudDomain cloudDomain = new CloudDomain();
        cloudDomain.setType(type);
        cloudDomain.setDomain(domain);
        cloudDomain.setCreateBy(String.valueOf(getUserId()));

        // 查询
        List<CloudDomain> list = cloudDomainMapper.page(pageDomain, cloudDomain);
        page.setRecords(list);
        page.setTotal(cloudDomainMapper.list(cloudDomain).size());
        return PageUtils.toPage(list);
    }

    /**
     * 添加
     * @param cloudDomain c
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "【新增】")
    public AjaxResult add(@RequestBody CloudDomain cloudDomain) {
        if (StrUtil.isBlank(cloudDomain.getDomain())) {
            return AjaxResult.error("域名不能为空");
        }
//        if (NetUtil.) {
//            return AjaxResult.error(cloudDomain.getDomainName() + "不是域名格式");
//        }
        cloudDomain.setCreateBy(String.valueOf(getUserId()));
        cloudDomain.setCreateTime(DateUtil.date());
        boolean result = cloudDomainService.save(cloudDomain);
        return result ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }

    /**
     * 删除
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "【删除】")
    public AjaxResult delete(@RequestParam(value = "id") String id) {
        boolean result = cloudDomainService.removeById(id);
        return result ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }
}
