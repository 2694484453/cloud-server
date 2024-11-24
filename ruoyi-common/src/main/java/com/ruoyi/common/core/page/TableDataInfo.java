package com.ruoyi.common.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author ruoyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 列表数据
     */
    private List<?> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    public TableDataInfo(long total, long totalPage, List<?> rows) {
        this.total = total;
        this.totalPage = totalPage;
        this.rows = rows;
        this.code = 200;
        this.msg = "success";
    }
}
