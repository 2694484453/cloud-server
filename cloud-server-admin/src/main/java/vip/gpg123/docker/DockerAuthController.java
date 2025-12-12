package vip.gpg123.docker;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.docker.domain.DockerAuth;
import vip.gpg123.docker.mapper.DockerAuthMapper;
import vip.gpg123.docker.service.DockerAuthService;

import java.util.List;

@RequestMapping("/docker/auth")
@RestController
public class DockerAuthController extends BaseController {

    @Autowired
    private DockerAuthService dockerAuthService;

    @Autowired
    private DockerAuthMapper dockerAuthMapper;

    /**
     * list
     * @param registryDomain r
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "registryDomain",required = false) String registryDomain) {
        List<DockerAuth> dockerAuths = dockerAuthService.list(new LambdaQueryWrapper<DockerAuth>()
                .eq(StrUtil.isNotBlank(registryDomain), DockerAuth::getRegistryDomain,registryDomain)
                .eq(DockerAuth::getCreateBy, SecurityUtils.getUserId())
                .orderByAsc(DockerAuth::getCreateTime)
        );
        return AjaxResult.success(dockerAuths);
    }

    /**
     * page
     * @param registryDomain r
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(@RequestParam(value = "registryDomain",required = false) String registryDomain) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<DockerAuth> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        DockerAuth search = new DockerAuth();
        search.setRegistryDomain(registryDomain);
        search.setCreateBy(SecurityUtils.getUserId().toString());
        //
        List<DockerAuth> list = dockerAuthMapper.page(pageDomain,search);
        page.setRecords(list);
        page.setTotal(dockerAuthMapper.list(search).size());
        return PageUtils.toPage(list);
    }
}
