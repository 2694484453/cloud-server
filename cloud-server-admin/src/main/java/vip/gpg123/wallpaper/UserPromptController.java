package vip.gpg123.wallpaper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.wallpaper.domain.UserPrompt;
import vip.gpg123.wallpaper.service.UserPromptService;

import java.util.List;

@RestController
@RequestMapping("/wallpaper/userPrompt")
public class UserPromptController {

    @Autowired
    private UserPromptService userPromptService;

    /**
     * list
     *
     * @param UserPrompt pg
     * @return r
     */
    @RequestMapping("/list")
    public AjaxResult list(UserPrompt UserPrompt) {
        List<UserPrompt> list = userPromptService.list(UserPrompt);
        return AjaxResult.success(list);
    }

    /**
     * page
     * @param page p
     * @param UserPrompt pg
     * @return r
     */
    @RequestMapping("/page")
    public TableDataInfo page(Page<UserPrompt> page, UserPrompt UserPrompt) {
        IPage<UserPrompt> result = userPromptService.page(page, UserPrompt);
        return PageUtils.toPageByIPage(result);
    }

}
