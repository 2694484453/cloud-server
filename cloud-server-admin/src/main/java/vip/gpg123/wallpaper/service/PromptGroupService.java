package vip.gpg123.wallpaper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.wallpaper.domain.PromptGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prompt_group】的数据库操作Service
* @createDate 2026-01-08 17:00:48
*/
public interface PromptGroupService extends IService<PromptGroup> {

    /**
     * page
     * @param page p
     * @param PromptGroup k
     * @return r
     */
    IPage<PromptGroup> page(Page<PromptGroup> page, PromptGroup PromptGroup);

    /**
     * list
     * @param PromptGroup key
     * @return r
     */
    List<PromptGroup> list(PromptGroup PromptGroup);
    
}
