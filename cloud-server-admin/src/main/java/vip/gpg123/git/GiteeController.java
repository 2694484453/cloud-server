package vip.gpg123.git;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/16 0:08
 **/
@RestController
@RequestMapping("/gitee")
@Api(tags = "gitee认证控制")
public class GiteeController {

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

    @Value("${git.gitee.access_token}")
    private String accessToken;

    /**
     * 获取accessToken
     *
     * @param code code
     * @return r
     */
    @PostMapping("/getAccessToken")
    @ApiOperation(value = "获取accessToken")
    private AjaxResult getAccessToken(@RequestParam("code") String code) {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.set("grant_type", grant_type);
        jsonObject.set("redirect_uri", redirect_uri);
        jsonObject.set("client_id", client_id);
        jsonObject.set("client_secret", client_secret);
        jsonObject.set("code", code);
        HttpResponse httpResponse = HttpUtil.createPost(api)
                .contentType(ContentType.JSON.getValue())
                .body(JSONUtil.toJsonStr(jsonObject))
                .timeout(5000)
                .execute();
        return AjaxResult.success(httpResponse.body());
    }

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo repos(){
        List<?> repoList = repoList();
        return PageUtils.toPage(repoList);
    }


    /**
     * 列表查询
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list(){
        List<?> repoList = repoList();
        return AjaxResult.success(repoList);
    }
    /**
     * 获取公开仓库
     * @return r
     */
    private List<?> repoList(){
        HttpResponse httpResponse = HttpUtil.createGet("https://gitee.com/api/v5/users/gpg-dev_admin/repos?access_token="+accessToken+"?&type=all&sort=full_name&page=1&per_page=100")
                .timeout(1000)
                .setConnectionTimeout(1000)
                .execute();
        return Convert.toList(httpResponse.body());
    }
}
