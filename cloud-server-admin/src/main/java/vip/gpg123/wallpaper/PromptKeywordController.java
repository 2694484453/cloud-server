package vip.gpg123.wallpaper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.wallpaper.domain.PromptKeyword;
import vip.gpg123.wallpaper.service.PromptKeywordService;

import java.util.List;

@RestController
@RequestMapping("/wallpaper/promptKeyword")
public class PromptKeywordController {

    @Autowired
    private PromptKeywordService PromptKeywordService;

    /**
     * list
     *
     * @param PromptKeyword pg
     * @return r
     */
    @RequestMapping("/list")
    public AjaxResult list(PromptKeyword PromptKeyword) {
        List<PromptKeyword> list = PromptKeywordService.list(PromptKeyword);
        return AjaxResult.success(list);
    }

    /**
     * page
     * @param page p
     * @param PromptKeyword pg
     * @return r
     */
    @RequestMapping("/page")
    public TableDataInfo page(Page<PromptKeyword> page, PromptKeyword PromptKeyword) {
        IPage<PromptKeyword> result = PromptKeywordService.page(page, PromptKeyword);
        return PageUtils.toPageByIPage(result);
    }


}
