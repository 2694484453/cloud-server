package vip.gpg123.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseBody implements Serializable {

    /**
     * 对象名称
     */
    private String name;

    /**
     * 操作
     */
    private String action;

    /**
     * 操作结果
     */
    private Boolean result;

}
