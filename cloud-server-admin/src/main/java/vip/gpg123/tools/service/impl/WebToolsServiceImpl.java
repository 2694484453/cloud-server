package vip.gpg123.tools.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.tools.domain.WebTools;
import vip.gpg123.tools.service.WebToolsService;
import vip.gpg123.tools.mapper.WebToolsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【web_tools(工具)】的数据库操作Service实现
 * @createDate 2025-10-23 02:24:22
 */
@Service
public class WebToolsServiceImpl extends ServiceImpl<WebToolsMapper, WebTools> implements WebToolsService {

    @Autowired
    private WebToolsMapper webToolsMapper;

    /**
     * list
     *
     * @param webTools w
     * @return r
     */
    @Override
    @DS("tools")
    public List<WebTools> list(WebTools webTools) {
        return webToolsMapper.list(webTools);
    }

    /**
     * page
     *
     * @param page     p
     * @param webTools w
     * @return r
     */
    @Override
    @DS("tools")
    public IPage<WebTools> page(Page<WebTools> page, WebTools webTools) {
        return webToolsMapper.page(page, webTools);
    }
}




