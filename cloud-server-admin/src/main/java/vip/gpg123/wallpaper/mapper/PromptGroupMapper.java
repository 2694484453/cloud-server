package vip.gpg123.wallpaper.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.wallpaper.domain.PromptGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【prompt_group】的数据库操作Mapper
 * @createDate 2026-01-08 17:00:48
 * @Entity vip.gpg123.wallpaper.domain.PromptGroup
 */
@Mapper
public interface PromptGroupMapper extends BaseMapper<PromptGroup> {

    /**
     * page
     *
     * @param page        page
     * @param PromptGroup key
     * @return r
     */
    IPage<PromptGroup> page(Page<PromptGroup> page, @Param("qw") PromptGroup PromptGroup);

    /**
     * list
     *
     * @param PromptGroup key
     * @return r
     */
    List<PromptGroup> list(@Param("qw") PromptGroup PromptGroup);

}




