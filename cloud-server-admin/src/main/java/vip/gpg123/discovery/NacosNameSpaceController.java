package vip.gpg123.discovery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.framework.config.domain.NacosClient;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/3/2 15:39
 **/
@RestController
@RequestMapping("/discovery/nacos/namespace")
@Api(tags = "【discovery】nacos命名空间管理")
public class NacosNameSpaceController {

    @Autowired
    private NacosClient nacosClient;

    /**
     * 列表查询
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<?> list = nacosClient.namespaceList();
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     * @param namespace 名称
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "namespace",required = false) String namespace){
        List<?> list = nacosClient.namespaceList();
        return PageUtils.toPage(list);
    }
}
