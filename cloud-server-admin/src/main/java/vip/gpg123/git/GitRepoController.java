package vip.gpg123.git;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import vip.gpg123.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.git.service.GitAccessService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/16 0:08
 **/
@RestController
@RequestMapping("/gitee")
@Api(tags = "【gitee】仓库管理")
public class GitRepoController {

    @Value("${git.gitee.grant_type}")
    private String grant_type;

    @Value("${git.gitee.redirect_uri}")
    private String redirect_uri;

    @Value("${git.gitee.client_id}")
    private String client_id;

    @Value("${git.gitee.client_secret}")
    private String client_secret;

    @Value("${git.gitee.api}")
    private String api;

    @Value("${git.gitee.username}")
    private String username;

    @Value("${git.gitee.access_token}")
    private String accessToken;

    @Autowired
    private GitAccessService gitAccessService;

    private static final String GITEE_REPO_URL = "https://gitee.com/api/v5/user/repos";

    /**
     * 获取type
     *
     * @return r
     */
    @GetMapping("/git/types")
    @ApiOperation(value = "获取type")
    public AjaxResult token() {
        List<String> types = new ArrayList<>();
        types.add("gitee");
        types.add("github");
        types.add("gitlab");
        types.add("github");
        types.add("gitcode");
        return AjaxResult.success(types);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo repos(@RequestParam(value = "type") String type) {
        List<?> repoList = giteerepoList();
        return PageUtils.toPage(repoList);
    }


    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list() {
        List<?> repoList = giteerepoList();
        return AjaxResult.success(repoList);
    }

    /**
     * 获取公开仓库
     *
     * @return r
     */
    private List<?> giteerepoList() {
        HttpResponse httpResponse = HttpUtil.createGet(GITEE_REPO_URL + "?access_token=" + accessToken + "&visibility=all&sort=full_name&page=1&per_page=9999")
                .timeout(10000)
                .setConnectionTimeout(10000)
                .execute();
        JSONArray jsonArray = JSONUtil.parseArray(httpResponse.body());
        return Convert.toList(jsonArray);
    }
}
