package vip.gpg123.wallpaper.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.wallpaper.domain.UserPrompt;
import vip.gpg123.wallpaper.service.UserPromptService;
import vip.gpg123.wallpaper.mapper.UserPromptMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【user_prompt(用户词条)】的数据库操作Service实现
* @createDate 2026-01-08 19:18:12
*/
@Service
public class UserPromptServiceImpl extends ServiceImpl<UserPromptMapper, UserPrompt> implements UserPromptService{

    @Autowired
    private UserPromptMapper userPromptMapper;

    /**
     * page
     *
     * @param page       page
     * @param UserPrompt key
     * @return r
     */
    @Override
    public IPage<UserPrompt> page(Page<UserPrompt> page, UserPrompt UserPrompt) {
        return userPromptMapper.page(page, UserPrompt);
    }

    /**
     * list
     *
     * @param UserPrompt key
     * @return r
     */
    @Override
    public List<UserPrompt> list(UserPrompt UserPrompt) {
        return userPromptMapper.list(UserPrompt);
    }
}




