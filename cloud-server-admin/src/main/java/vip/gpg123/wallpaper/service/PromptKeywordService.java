package vip.gpg123.wallpaper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.wallpaper.domain.PromptKeyword;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【prompt_keyword】的数据库操作Service
 * @createDate 2026-01-08 17:00:51
 */
public interface PromptKeywordService extends IService<PromptKeyword> {

    /**
     * page
     * @param page p
     * @param promptKeyword k
     * @return r
     */
    IPage<PromptKeyword> page(Page<PromptKeyword> page, PromptKeyword promptKeyword);

    /**
     * list
     * @param promptKeyword key
     * @return r
     */
    List<PromptKeyword> list(PromptKeyword promptKeyword);
}
