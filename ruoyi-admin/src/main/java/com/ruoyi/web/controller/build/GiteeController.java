package com.ruoyi.web.controller.build;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
