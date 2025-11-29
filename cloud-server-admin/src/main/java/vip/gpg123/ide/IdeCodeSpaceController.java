package vip.gpg123.ide;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.AnyObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.client.IdeClient;
import vip.gpg123.ide.domain.IdeCodeOpen;
import vip.gpg123.ide.domain.IdeCodeSpace;
import vip.gpg123.ide.service.IdeCodeSpaceService;

import java.util.Collection;
import java.util.List;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2025/2/14 15:44
 */
@RestController
@RequestMapping("/ide/codeSpace")
@Api(tags = "【ide】code空间管理")
public class IdeCodeSpaceController {

    @Autowired
    private IdeCodeSpaceService codeSpaceService;

    @Autowired
    private IdeClient ideClient;

    /**
     * 分页查询
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name", required = false) String name) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        IPage<IdeCodeSpace> page = new Page<>();
        page.setCurrent(pageDomain.getPageNum());
        page.setSize(pageDomain.getPageSize());
        page = codeSpaceService.page(page, new LambdaQueryWrapper<IdeCodeSpace>()
                .like(StrUtil.isNotBlank(name), IdeCodeSpace::getName, name)
                .orderByDesc(IdeCodeSpace::getCreateTime)
        );
        return PageUtils.toPageByIPage(page);
    }

    /**
     * 分页查询
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name) {
        List<?> list = codeSpaceService.list(new LambdaQueryWrapper<IdeCodeSpace>()
                .like(StrUtil.isNotBlank(name), IdeCodeSpace::getName, name)
                .orderByDesc(IdeCodeSpace::getCreateTime));
        return AjaxResult.success(list);
    }

    /**
     * 新增
     *
     * @param ideCodeSpace i
     * @return r
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增")
    public AjaxResult add(@RequestBody IdeCodeSpace ideCodeSpace) {
        ideCodeSpace.setCreateBy(SecurityUtils.getUsername());
        ideCodeSpace.setWorkPath("/home/coder/" + SecurityUtils.getUsername() + "/" + ideCodeSpace.getName());
        boolean isSuccess = codeSpaceService.save(ideCodeSpace);
        return isSuccess ? AjaxResult.success("新增成功", true) : AjaxResult.error("新增失败", false);
    }

    /**
     * 打开工作空间
     *
     * @param ideCodeOpen i
     * @return r
     */
    @PostMapping("/open")
    @ApiOperation(value = "打开")
    public AjaxResult open(@RequestBody IdeCodeOpen ideCodeOpen) {
        // 检查名称
        String name = ideCodeOpen.getName();
        IdeCodeSpace ideCodeSpace = codeSpaceService.getOne(new LambdaQueryWrapper<IdeCodeSpace>()
                .eq(StrUtil.isNotBlank(name), IdeCodeSpace::getName, name)
        );
        // 判断是否存在
        if (ObjectUtil.isNotNull(ideCodeSpace)) {
            return AjaxResult.success(ideCodeSpace);
        } else {
            // 进行克隆下载
            String parentDir = ideClient.getPath() + "/" + SecurityUtils.getUsername();
            String targetDir = parentDir + "/" + name;
            try {
                Git git = Git.cloneRepository()
                        .setBranch(ideCodeOpen.getBranch())
                        .setDirectory(FileUtil.file(targetDir))
                        .setCallback(new CloneCommand.Callback() {
                            /**
                             * 初始化回调
                             * @param submodules
                             * the submodules
                             */
                            @Override
                            public void initializedSubmodules(Collection<String> submodules) {
                                Console.log("[}-initializedSubmodules", name);
                            }

                            @Override
                            public void cloningSubmodule(String path) {
                                Console.log("[}-cloningSubmodule", name);
                            }

                            @Override
                            public void checkingOut(AnyObjectId commit, String path) {
                                Console.log("[}-checkingOut", name);
                            }
                        }).setURI(ideCodeOpen.getHtmlUrl())
                        .call();
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            } finally {
                // 新增记录
                IdeCodeSpace ideCodeSpaceSave = new IdeCodeSpace();
                ideCodeSpaceSave.setCreateBy(SecurityUtils.getUsername());
                ideCodeSpaceSave.setCreateTime(DateUtil.date());
                ideCodeSpaceSave.setName(name);
                ideCodeSpaceSave.setGitHttp(ideCodeOpen.getHtmlUrl());
                ideCodeSpaceSave.setDescription(ideCodeOpen.getDescription());
                boolean isSuccess = codeSpaceService.saveOrUpdate(ideCodeSpaceSave);
                Console.log("插入结果：{}", isSuccess);
                // 发送邮件
                sendEmail(ideCodeOpen.getName());
            }
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 修改
     *
     * @param ideCodeSpace i
     * @return r
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改")
    public AjaxResult edit(@RequestBody IdeCodeSpace ideCodeSpace) {
        boolean isSuccess = codeSpaceService.updateById(ideCodeSpace);
        return isSuccess ? AjaxResult.success("修改成功", true) : AjaxResult.error("修改失败", false);
    }

    /**
     * 删除
     *
     * @param name 名称
     * @return r
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public AjaxResult delete(@RequestParam("name") String name) {
        boolean isSuccess = codeSpaceService.removeById(name);
        return isSuccess ? AjaxResult.success("删除成功", true) : AjaxResult.error("删除失败", false);
    }

    @Async
    public void sendEmail(String workSpace) {
       // MailUtil.send(mailAccount, SecurityUtils.getLoginUser().getUser().getEmail(), "云开发工作空间变动提醒", "尊敬的用户:" + SecurityUtils.getUsername() + ",您的" + workSpace + "已创建成功！", false);
    }
}
