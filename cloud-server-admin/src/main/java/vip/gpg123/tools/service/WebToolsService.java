package vip.gpg123.tools.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.tools.domain.WebTools;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【web_tools(工具)】的数据库操作Service
 * @createDate 2025-10-23 02:24:22
 */
public interface WebToolsService extends IService<WebTools> {

    /**
     * list
     * @param webTools w
     * @return r
     */
    List<WebTools> list(WebTools webTools);

    /**
     * page
     * @param page p
     * @param webTools w
     * @return r
     */
    IPage<WebTools> page(Page<WebTools> page, WebTools webTools);

}
