package com.ruoyi.git;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.git.domain.Git;
import com.ruoyi.git.domain.Gitee;
import com.ruoyi.git.service.GitService;
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
    public AjaxResult list(@RequestParam(value = "type",required = false) String type,
                           @RequestParam(value = "name",required = false) String name) {
        List<Git> gitList = gitService.list(new LambdaQueryWrapper<Git>()
                .eq(StrUtil.isNotBlank(type),Git::getType,type)
                .eq(StrUtil.isNotBlank(name),Git::getName,name)
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
    public TableDataInfo page(@RequestParam(value = "type",required = false) String type,
                              @RequestParam(value = "name",required = false) String name) {
        AjaxResult ajaxResult = list(type,name);
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }

    /**
     * 获取token
     * @param code code
     * @return r
     */
    @PostMapping("/token")
    @ApiOperation(value = "获取token")
    public AjaxResult token(@RequestParam(value = "code") String code) {
        HttpResponse response = HttpUtil.createPost(gitee.getApi())
                .form("client_id", gitee.getClient_id())
                .form("grant_type", gitee.getGrant_type())
                .form("redirect_uri", gitee.getRedirect_uri())
                .body(JSONUtil.toJsonStr(gitee.getClient_secret()))
                .execute(false);
        return AjaxResult.success("请求成功", response.body());
    }
}
