package vip.gpg123.ide;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
import vip.gpg123.git.domain.IdeCodeSpace;
import vip.gpg123.git.service.IdeCodeSpaceService;

import java.util.List;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2025/2/14 15:44
 */
@RestController
@RequestMapping("/ide")
@Api(tags = "【ide】code空间管理")
public class IdeCodeSpaceController {

    @Autowired
    private IdeCodeSpaceService codeSpaceService;

    /**
     * 分页查询
     *
     * @param name 名称
     * @return r
     */
    @GetMapping("/page")
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
    public AjaxResult add(@RequestBody IdeCodeSpace ideCodeSpace) {
        boolean isSuccess = codeSpaceService.save(ideCodeSpace);
        return isSuccess ? AjaxResult.success("新增成功", true) : AjaxResult.error("新增失败", false);
    }

    /**
     * 修改
     * @param ideCodeSpace i
     * @return r
     */
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody IdeCodeSpace ideCodeSpace) {
        boolean isSuccess = codeSpaceService.updateById(ideCodeSpace);
        return isSuccess ? AjaxResult.success("修改成功", true) : AjaxResult.error("修改失败", false);
    }

    /**
     * 删除
     * @param name 名称
     * @return r
     */
    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestParam("name") String name) {
        boolean isSuccess = codeSpaceService.removeById(name);
        return isSuccess ? AjaxResult.success("删除成功", true) : AjaxResult.error("删除失败", false);
    }
}
