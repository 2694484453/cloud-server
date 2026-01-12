package vip.gpg123.wallpaper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.wallpaper.domain.PromptGroup;
import vip.gpg123.wallpaper.service.PromptGroupService;

import java.util.List;

@RestController
@RequestMapping("/wallpaper/promptGroup")
public class PromptGroupController {

    @Autowired
    private PromptGroupService promptGroupService;

    /**
     * list
     *
     * @param promptGroup pg
     * @return r
     */
    @RequestMapping("/list")
    public AjaxResult list(PromptGroup promptGroup) {
        List<PromptGroup> list = promptGroupService.list(promptGroup);
        return AjaxResult.success(list);
    }

    /**
     * page
     * @param page p
     * @param promptGroup pg
     * @return r
     */
    @RequestMapping("/page")
    public TableDataInfo page(Page<PromptGroup> page, PromptGroup promptGroup) {
        IPage<PromptGroup> result = promptGroupService.page(page, promptGroup);
        return PageUtils.toPageByIPage(result);
    }
}
