package vip.gpg123.app;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.app.domain.HelmEntity;
import vip.gpg123.app.service.impl.HelmApi;

@RestController
@RequestMapping("/helm/api")
public class HelmApiController {

    @Autowired
    private HelmApi helmApi;

    /**
     * values
     *
     * @param entity e
     * @return r
     */
    @PostMapping("/values")
    @ApiOperation(value = "渲染参数")
    public Object values(@RequestBody HelmEntity entity) {
        return helmApi.values(entity);
    }

    /**
     * readme
     * @param entity e
     * @return r
     */
    @PostMapping("/readme")
    @ApiOperation(value = "查看readme")
    public Object readme(@RequestBody HelmEntity entity) {
        return helmApi.values(entity);
    }

    /**
     * 安装
     */
    @PostMapping("/install")
    @ApiOperation(value = "安装")
    public Object install(@RequestBody HelmEntity entity) {
        return helmApi.install(entity);
    }

    /**
     * 更新
     * @param entity e
     * @return r
     */
    @PostMapping("/upgrade")
    @ApiOperation(value = "更新")
    public Object upgrade(@RequestBody HelmEntity entity) {
        return helmApi.upgrade(entity);
    }

    /**
     * 卸载
     */
    @PostMapping("/uninstall")
    @ApiOperation(value = "卸载")
    public Object uninstall(@RequestBody HelmEntity entity) {
        return helmApi.uninstall(entity);
    }
}
