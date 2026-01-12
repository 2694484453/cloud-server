package vip.gpg123.wallpaper.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.wallpaper.domain.PromptKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prompt_keyword】的数据库操作Mapper
* @createDate 2026-01-08 17:00:51
* @Entity vip.gpg123.wallpaper.domain.PromptKeyword
*/
@Mapper
public interface PromptKeywordMapper extends BaseMapper<PromptKeyword> {

    /**
     * page
     * @param page page
     * @param promptKeyword key
     * @return r
     */
    IPage<PromptKeyword> page(Page<PromptKeyword> page, @Param("qw")  PromptKeyword promptKeyword);

    /**
     * list
     * @param promptKeyword key
     * @return r
     */
    List<PromptKeyword> list(@Param("qw") PromptKeyword promptKeyword);

}




