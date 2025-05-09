package vip.gpg123.devops;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/devops/common")
@Api(tags = "【devops】通用管理")
public class DevopsCommonController extends BaseController {

    /**
     * 获取构建类型
     *
     * @return r
     */
    @GetMapping("/types")
    @ApiOperation(value = "获取构建类型")
    public AjaxResult types() {
        List<String> types = new ArrayList<>();
        // java构建
        types.add("maven");
        types.add("maven+docker");
        // gradle构建
        types.add("gradle");
        types.add("gradle+docker");
        // nodejs构建
        types.add("nodejs");
        types.add("nodejs+docker");
        // python构建
        types.add("python");
        types.add("python+docker");
        // shell构建
        types.add("shell");
        types.add("shell+docker");
        // 镜像构建
        types.add("docker");
        return AjaxResult.success(types);
    }

    /**
     * 获取镜像
     * @param type 类型
     * @return r
     */
    @GetMapping("/images")
    @ApiOperation(value = "获取镜像")
    public AjaxResult images(@RequestParam(value = "type",required = false) String type) {
        List<String> images = new ArrayList<>();
        switch (type) {
            case "maven":
            case "maven+docker":
                images.add("registry.cn-hangzhou.aliyuncs.com/gpg_dev/maven:3.8.4-openjdk-8");
                images.add("registry.cn-hangzhou.aliyuncs.com/gpg_dev/maven:3.8.4-openjdk-11");
                images.add("registry.cn-hangzhou.aliyuncs.com/gpg_dev/maven:3.8.4-openjdk-17");
                break;
            case "gradle":
            case "gradle+docker":
                images.add("registry.cn-hangzhou.aliyuncs.com/gpg_dev/gradle:3.8.4-openjdk-8");
                images.add("registry.cn-hangzhou.aliyuncs.com/gpg_dev/gradle:3.8.4-openjdk-11");
                break;
            case "nodejs":
                images.add("nodejs");
                images.add("nodejs+docker");
                break;
            case "python":
                images.add("python");
                images.add("python+docker");
            default:
                break;
        }
        return AjaxResult.success(images);
    }
}
