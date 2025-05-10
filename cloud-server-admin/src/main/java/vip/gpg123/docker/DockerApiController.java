package vip.gpg123.docker;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.docker.domain.DockerRepo;
import vip.gpg123.docker.service.DockerRepoService;

@RestController
@RequestMapping("/docker/api")
@Api(tags = "docker-api管理")
public class DockerApiController extends BaseController {

    @Autowired
    private DockerRepoService dockerRepoService;

    /**
     * 登录
     * @param dockerRepo 参数
     * @return r
     */
    @PostMapping("/login")
    @ApiOperation(value = "【登录】")
    public AjaxResult login(@RequestBody DockerRepo dockerRepo) {
        DockerRepo repo = dockerRepoService.getOne(new LambdaQueryWrapper<DockerRepo>()
                .eq(StrUtil.isNotBlank(dockerRepo.getRepoName()), DockerRepo::getRepoName, dockerRepo.getRepoName())
                .eq(StrUtil.isNotBlank(dockerRepo.getId()), DockerRepo::getId, dockerRepo.getId())
                .eq(DockerRepo::getCreateBy, getUsername())
        );
        if (repo == null) {
            String res = RuntimeUtil.execForStr("/bin/sh", "-c", "docker login --username " + dockerRepo.getRepoName() + " --password " + dockerRepo.getPassword() + " " + dockerRepo.getRepoHost());
            return res.contains("success") ? AjaxResult.success() : AjaxResult.error();
        }
        return AjaxResult.error("没有查询到仓库配置");
    }
}
