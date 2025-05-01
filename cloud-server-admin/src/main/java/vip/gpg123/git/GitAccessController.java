package vip.gpg123.git;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
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
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.domain.Email;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.git.domain.GitAccess;
import vip.gpg123.git.service.GitAccessService;

import java.util.List;
import java.util.TimerTask;

/**
 * @author gpg
 * @date 2020/6/29
 * @description Git仓库认证管理
 */
@RestController
@RequestMapping("/gitAccess")
@Api(value = "Git仓库认证管理")
public class GitAccessController extends BaseController {

    @Autowired
    private GitAccessService gitAccessService;


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
        List<GitAccess> gitAccessList = gitAccessService.list(new LambdaQueryWrapper<GitAccess>()
                .eq(StrUtil.isNotBlank(type), GitAccess::getType, type)
                .like(StrUtil.isNotBlank(name), GitAccess::getName, name)
                .eq(GitAccess::getCreateBy, getUsername())
                .orderByDesc(GitAccess::getCreateTime)
        );
        return AjaxResult.success(gitAccessList);
    }

    /**
     * 分页查询
     *
     * @param name 名称
     * @param type 类型
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "type", required = false) String type) {
        IPage<GitAccess> page = new Page<>(TableSupport.buildPageRequest().getPageNum(), TableSupport.buildPageRequest().getPageSize());
        page = gitAccessService.page(page, new LambdaQueryWrapper<GitAccess>()
                .eq(StrUtil.isNotBlank(type), GitAccess::getType, type)
                .like(StrUtil.isNotBlank(name), GitAccess::getName, name)
                .eq(GitAccess::getCreateBy, getUsername()));
        return PageUtils.toPageByIPage(page);
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
        GitAccess gitAccess = gitAccessService.getById(id);
        return AjaxResult.success(gitAccess);
    }

    /**
     * 添加
     *
     * @param gitAccess gitAccess
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加")
    public AjaxResult add(@RequestBody GitAccess gitAccess) {
        // 查询
        int count = gitAccessService.count(new LambdaQueryWrapper<GitAccess>()
                .eq(GitAccess::getCreateBy, getUsername())
                .eq(GitAccess::getType, gitAccess.getType())
        );
        // 判断是否已经添加
        if (count >= 1) {
            return AjaxResult.error("您已经添加过" + gitAccess.getType() + "类型的认证，请勿重复添加");
        }
        // 获取邮箱
        String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
        // 获取用户名
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        gitAccess.setCreateBy(getUsername());
        gitAccess.setCreateTime(DateUtil.date());
        boolean isSuccess = gitAccessService.save(gitAccess);
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 组装消息
                Email email = new Email();
                String[] tos = new String[]{};
                tos = ArrayUtil.append(tos, userEmail);
                email.setTos(tos);
                email.setTitle("Git仓库认证添加通知");
                email.setContent(userName + "添加" + gitAccess.getName() + "Git仓库认证，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改
     *
     * @param gitAccess gitAccess
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody GitAccess gitAccess) {
        // 获取邮箱
        String userEmail = SecurityUtils.getLoginUser().getUser().getEmail();
        // 获取用户名
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        gitAccess.setUpdateBy(getUsername());
        gitAccess.setUpdateTime(DateUtil.date());
        boolean isSuccess = gitAccessService.updateById(gitAccess);
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 组装消息
                Email email = new Email();
                String[] tos = new String[]{};
                tos = ArrayUtil.append(tos, userEmail);
                email.setTos(tos);
                email.setTitle("Git仓库认证修改通知");
                email.setContent(userName + "修改" + gitAccess.getName() + "Git仓库认证，结果：" + (isSuccess ? "成功" : "失败"));
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
        boolean isSuccess = gitAccessService.removeById(id);
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 获取GitAccess
                GitAccess gitAccess = gitAccessService.getById(id);
                // 组装消息
                Email email = new Email();
                String[] tos = new String[]{};
                tos = ArrayUtil.append(tos, userEmail);
                email.setTos(tos);
                email.setTitle("Git仓库认证删除通知");
                email.setContent(userName + "删除" + gitAccess.getName() + "Git仓库认证，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess ? AjaxResult.success() : AjaxResult.error();
    }
}
