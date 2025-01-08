package vip.gpg123.git;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.git.domain.Git;
import vip.gpg123.git.domain.Gitee;
import vip.gpg123.git.service.GitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:48
 **/
@RestController
@RequestMapping("/git")
@Api(tags = "git仓库")
public class GitController {

    @Autowired
    private Gitee gitee;

    @Autowired
    private GitService gitService;

    private static final String path = "/root/.my-server";

    /**
     * 列表查询
     *
     * @param type 类型
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "type", required = false) String type,
                           @RequestParam(value = "name", required = false) String name) {
        List<Git> gitList = gitService.list(new LambdaQueryWrapper<Git>()
                .eq(StrUtil.isNotBlank(type), Git::getType, type)
                .eq(StrUtil.isNotBlank(name), Git::getName, name)
        );
        return AjaxResult.success(gitList);
    }

    /**
     * 分页查询
     *
     * @param type 类型
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "type", required = false) String type,
                              @RequestParam(value = "name", required = false) String name) {
        AjaxResult ajaxResult = list(type, name);
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }

    /**
     * 获取token
     *
     * @param code code
     * @return r
     */
    @PostMapping("/gitee/token")
    @ApiOperation(value = "获取token")
    public AjaxResult token(@RequestParam(value = "code") String code) {
        String url = gitee.getApi() + "?client_id=" + gitee.getClient_id() + "&grant_type=" + gitee.getGrant_type() + "&redirect_uri=" + gitee.getRedirect_uri() + "&code=" + code + "&client_secret=" + gitee.getClient_secret();
        String body = HttpUtil.post(url, "{}");
        JSONObject jsonObject = JSONUtil.parseObj(body);
        return AjaxResult.success("请求成功", jsonObject);
    }
}
