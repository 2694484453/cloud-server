package vip.gpg123.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.repo.domain.HelmRepo;
import vip.gpg123.repo.service.HelmRepoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/common")
public class AppCommonController extends BaseController {

    @Autowired
    private HelmRepoService helmRepoService;


}
