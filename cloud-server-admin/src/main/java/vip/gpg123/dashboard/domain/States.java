package vip.gpg123.dashboard.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName states
 */
@TableName(value ="states")
@Data
public class States implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属国家代码
     */
    @TableField(value = "country_id")
    private Integer countryId;

    /**
     * 
     */
    @TableField(value = "code")
    private String code;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "cname")
    private String cname;

    /**
     * 
     */
    @TableField(value = "lower_name")
    private String lowerName;

    /**
     * 
     */
    @TableField(value = "code_full")
    private String codeFull;

    /**
     * ISO 3166-2 编码，如 CN-HA
     */
    @TableField(value = "iso_code")
    private String isoCode;

    /**
     * 
     */
    @TableField(value = "area_id")
    private Integer areaId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}