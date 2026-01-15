package vip.gpg123.tools.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.tools.domain.WebTools;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【web_tools(工具)】的数据库操作Mapper
 * @createDate 2025-10-23 02:24:22
 * @Entity vip.gpg123.tools.domain.WebTools
 */
@Mapper
public interface WebToolsMapper extends BaseMapper<WebTools> {

    /**
     * list
     *
     * @param webTools w
     * @return r
     */
    List<WebTools> list(@Param("qw") WebTools webTools);

    /**
     * page
     *
     * @param page     p
     * @param webTools w
     * @return r
     */
    IPage<WebTools> page(Page<WebTools> page, @Param("qw") WebTools webTools);
}




