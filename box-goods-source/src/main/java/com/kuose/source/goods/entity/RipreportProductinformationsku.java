package com.kuose.source.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
@Data
@TableName("ripreport_ProductInformationSku")
public class RipreportProductinformationsku extends Model<RipreportProductinformationsku> {

private static final long serialVersionUID=1L;

    @TableId(value = "autoid", type = IdType.AUTO)
    private Integer autoid;

    private String productno;

    @TableField("colorTableAutoid")
    private Integer colorTableAutoid;

    private String color;

    @TableField("colorCode")
    private String colorCode;

    @TableField("sizeCodeTableAutoid")
    private Integer sizeCodeTableAutoid;

    @TableField("sizeCodeTableCode")
    private String sizeCodeTableCode;

    @TableField("sizeCodeTableName")
    private String sizeCodeTableName;

    @TableField("skuCode")
    private String skuCode;

    private Integer flag;

    @TableField("gbCode")
    private String gbCode;

    private Double weights;

    @TableField("weightUnit")
    private String weightUnit;

    @TableField("useState")
    private Integer useState;

    @TableField("productInformationAutoid")
    private Integer productInformationAutoid;

    private Integer sort;

    private String msreplTranVersion;

    @TableField("gbCodeCreateTime")
    private String gbCodeCreateTime;

    @TableField("tagPrice")
    private Double tagPrice;

    private Integer success;

    private Integer failure;

    @TableField("webPrice")
    private Double webPrice;

    @TableField("hdPrice")
    private Double hdPrice;

    @TableField("vipPrice")
    private Double vipPrice;

}
