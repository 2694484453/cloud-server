package vip.gpg123.git;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/5 2:59
 **/
@RestController
@RequestMapping("/github")
@Api(tags = "【github】仓库管理")
public class GithubController {

    @Value("${git.github.username}")
    private String username;

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo page() {
        List<?> list = repoList();
        return PageUtils.toPage(list);
    }

    /**
     * 列表查询
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "【列表查询】")
    public AjaxResult list() {
        return AjaxResult.success(repoList());
    }

    /**
     * 获取仓库列表
     * @return r
     */
    private List<?> repoList() {
        HttpResponse httpResponse = HttpUtil.createGet("https://api.github.com/users/" + username + "/repos")
                .timeout(10000)
                .setConnectionTimeout(10000)
                .execute();
        JSONArray jsonArray = JSONUtil.parseArray(httpResponse.body());
        return Convert.toList(jsonArray);
    }
}
