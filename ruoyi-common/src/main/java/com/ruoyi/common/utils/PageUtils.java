package com.ruoyi.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.JSONArray;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.sql.SqlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @author ruoyi
 */
public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    public static TableDataInfo toPage(List<?> list) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        //
        int total = list.size();
        int totalPages = (total + pageSize - 1) / pageNum;
        if (pageNum > totalPages) {
            pageNum = totalPages;
        }
        int startIndex = PageUtil.getStart(pageNum, pageSize);
        int endIndex = PageUtil.getEnd(pageNum, pageSize);
        int totalPage = PageUtil.totalPage(total, pageSize);
        return new TableDataInfo(total, totalPage, list.subList(startIndex, endIndex));
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }
}
