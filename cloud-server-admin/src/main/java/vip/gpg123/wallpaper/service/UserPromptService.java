package vip.gpg123.wallpaper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.wallpaper.domain.UserPrompt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【user_prompt(用户词条)】的数据库操作Service
 * @createDate 2026-01-08 19:18:12
 */
public interface UserPromptService extends IService<UserPrompt> {

    /**
     * page
     *
     * @param page       page
     * @param UserPrompt key
     * @return r
     */
    IPage<UserPrompt> page(Page<UserPrompt> page, UserPrompt UserPrompt);

    /**
     * list
     *
     * @param UserPrompt key
     * @return r
     */
    List<UserPrompt> list(UserPrompt UserPrompt);

}
