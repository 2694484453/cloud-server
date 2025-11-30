package vip.gpg123.git;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.git.domain.GitToken;
import vip.gpg123.git.mapper.GitTokenMapper;
import vip.gpg123.git.service.GitTokenService;

import java.util.List;
import java.util.TimerTask;

/**
 * @author gpg
 * @date 2020/6/29
 * @description Git仓库认证管理
 */
@RestController
@RequestMapping("/git/token")
@Api(value = "Git仓库认证管理")
public class GitTokenController extends BaseController {

    @Autowired
    private GitTokenService gitTokenService;

    @Autowired
    private GitTokenMapper gitTokenMapper;

    /**
     * 列表查询
     *
     * @param name 名称
     * @param type 类型
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "type", required = false) String type) {
        List<GitToken> gitTokenList = gitTokenService.list(new LambdaQueryWrapper<GitToken>()
                .eq(StrUtil.isNotBlank(type), GitToken::getType, type)
                .like(StrUtil.isNotBlank(name), GitToken::getName, name)
                .eq(GitToken::getCreateBy, getUsername())
                .orderByDesc(GitToken::getCreateTime)
        );
        return AjaxResult.success(gitTokenList);
    }

    /**
     * 分页查询
     *
     * @param name 名称
     * @param type 类型
     * @return r
     */
    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "【分页查询】")
    public TableDataInfo repos(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "type", required = false) String type) {
        // 转换参数
        PageDomain pageDomain = TableSupport.buildPageRequest();
        pageDomain.setOrderByColumn(StrUtil.toUnderlineCase(pageDomain.getOrderByColumn()));
        IPage<GitToken> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        // 获取token
        GitToken gitToken = new GitToken();
        gitToken.setType(type);
        gitToken.setName(name);
        gitToken.setCreateBy(String.valueOf(getUserId()));

        // 查询
        List<GitToken> list = gitTokenMapper.page(pageDomain, gitToken);
        page.setRecords(list);
        page.setTotal(gitTokenMapper.list(gitToken).size());
        return PageUtils.toPage(list);
    }

    /**
     * 详情查询
     *
     * @param id id
     * @return r
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情查询")
    public AjaxResult info(@RequestParam(value = "id") String id) {
        GitToken gitToken = gitTokenService.getById(id);
        return AjaxResult.success(gitToken);
    }

    /**
     * 添加
     *
     * @param gitToken gitAccess
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加")
    public AjaxResult add(@RequestBody GitToken gitToken) {
        // 查询
        int count = gitTokenService.count(new LambdaQueryWrapper<GitToken>()
                .eq(GitToken::getCreateBy, getUsername())
                .eq(GitToken::getType, gitToken.getType())
        );
        // 判断是否已经添加
        if (count >= 1) {
            return AjaxResult.error("您已经添加过" + gitToken.getType() + "类型的认证，请勿重复添加");
        }
        // 获取邮箱
        String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
        // 获取用户名
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        gitToken.setCreateBy(getUsername());
        gitToken.setCreateTime(DateUtil.date());
        boolean isSuccess = gitTokenService.save(gitToken);
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 组装消息
//                Email email = new Email();
//                String[] tos = new String[]{};
//                tos = ArrayUtil.append(tos, userEmail);
//                email.setTos(tos);
//                email.setTitle("Git仓库认证添加通知");
//                email.setContent(userName + "添加" + gitAccess.getName() + "Git仓库认证，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     *
     * @param gitToken gitAccess
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody GitToken gitToken) {
        // 获取邮箱
        String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
        // 获取用户名
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        gitToken.setUpdateBy(getUsername());
        gitToken.setUpdateTime(DateUtil.date());
        boolean isSuccess = gitTokenService.updateById(gitToken);
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 组装消息
//                Email email = new Email();
//                String[] tos = new String[]{};
//                tos = ArrayUtil.append(tos, userEmail);
//                email.setTos(tos);
//                email.setTitle("Git仓库认证修改通知");
//                email.setContent(userName + "修改" + gitAccess.getName() + "Git仓库认证，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 删除
     *
     * @param id id
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam(value = "id") String id) {
        // 获取邮箱
        String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
        // 获取用户名
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        boolean isSuccess = gitTokenService.removeById(id);
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 获取GitAccess
                GitToken gitToken = gitTokenService.getById(id);
                // 组装消息
//                Email email = new Email();
//                String[] tos = new String[]{};
//                tos = ArrayUtil.append(tos, userEmail);
//                email.setTos(tos);
//                email.setTitle("Git仓库认证删除通知");
//                email.setContent(userName + "删除" + gitAccess.getName() + "Git仓库认证，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }
}
