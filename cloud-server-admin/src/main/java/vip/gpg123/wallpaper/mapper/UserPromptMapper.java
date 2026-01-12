package vip.gpg123.wallpaper.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.wallpaper.domain.UserPrompt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【user_prompt(用户词条)】的数据库操作Mapper
 * @createDate 2026-01-08 19:18:12
 * @Entity vip.gpg123.wallpaper.domain.UserPrompt
 */
@Mapper
public interface UserPromptMapper extends BaseMapper<UserPrompt> {

    /**
     * page
     *
     * @param page       page
     * @param UserPrompt key
     * @return r
     */
    IPage<UserPrompt> page(Page<UserPrompt> page, @Param("qw") UserPrompt UserPrompt);

    /**
     * list
     *
     * @param UserPrompt key
     * @return r
     */
    List<UserPrompt> list(@Param("qw") UserPrompt UserPrompt);

}




