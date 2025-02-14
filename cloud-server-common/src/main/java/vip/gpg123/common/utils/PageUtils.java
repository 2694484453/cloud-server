package vip.gpg123.common.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageHelper;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.page.TableSupport;
import vip.gpg123.common.utils.sql.SqlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @author gpg123
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

    /**
     * 手动分页处理
     *
     * @param list list
     * @return r
     */
    public static TableDataInfo toPage(List<?> list) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        //
        int total = list.size();
        PageUtil.setFirstPageNo(1);
        int startIndex = PageUtil.getStart(pageNum, pageSize);
        int endIndex = PageUtil.getEnd(pageNum, pageSize);
        int totalPage = PageUtil.totalPage(total, pageSize);
        if (total - 1 <= endIndex) {
            endIndex = total - 1;
            endIndex = endIndex + 1;
        }
        list = ListUtil.sub(list, startIndex, endIndex);
        return new TableDataInfo(total, totalPage, list);
    }

    /**
     * iPage 转table
     *
     * @param iPage i
     * @return r
     */
    public static TableDataInfo toPageByIPage(IPage<?> iPage) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("操作成功");
        rspData.setRows(iPage.getRecords());
        rspData.setTotal(iPage.getTotal());
        return rspData;
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }
}
