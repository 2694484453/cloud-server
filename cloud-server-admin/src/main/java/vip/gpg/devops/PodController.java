package vip.gpg.devops;

import cn.hutool.core.convert.Convert;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/12/3 23:34
 **/
@RestController
@RequestMapping("/podLog")
@Api(tags = "pod控制管理")
public class PodController {

    /**
     * 列表查询
     *
     * @param podName   pod
     * @param nameSpace ns
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "podName", required = false) String podName,
                           @RequestParam(value = "nameSpace", required = false) String nameSpace) {
        List<Pod> pods = K8sUtil.createKClient().pods().inAnyNamespace().list().getItems();
        return AjaxResult.success("查询成功", pods);
    }

    /**
     * 分页查询
     * @param podName pod
     * @param nameSpace ns
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "podName", required = false) String podName,
                              @RequestParam(value = "nameSpace", required = false) String nameSpace) {
        List<?> list = Convert.toList(list(podName, nameSpace).get("data"));
        return PageUtils.toPage(list);
    }
}
