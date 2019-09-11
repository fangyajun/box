package com.kuose.source.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 商品基本信息表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Data
@TableName("ripreport_ProductInformation")
public class BoxGoods extends Model<BoxGoods> {

private static final long serialVersionUID=1L;

    private Integer id;

    @TableId(value = "autoid", type = IdType.AUTO)
    private Integer sourceGoodsId;

    /**
     * 商品编号/货号
     */
    @TableField("productno")
    private String goodsNo;

    /**
     * 商品名称
     */
    @TableField("thirdCategoryStr")
    private String name;

    /**
     * 商品品牌
     */
    @TableField("firstCategory")
    private String brand;

    /**
     * 商品所属类目ID
     */
    @TableField("secondCategory")
    private String categoryCode;

    /**
     * 商品宣传图片列表，采用JSON数组格式
     */
    @TableField("img")
    private String img;

    /**
     * 商品天猫链接
     */
    @TableField("productUrl")
    private String tmallUrl;

    /**
     * 季节
     */
    @TableField("quarter")
    private String quarter;

    /**
     * 年份
     */
    @TableField("year")
    private String year;


    /**
     * 专柜价格
     */
    @TableField("webcost")
    private BigDecimal counterPrice;

    /**
     * 吊牌价格
     */
    @TableField("tagcost")
    private BigDecimal tagPrice;

    /**
     * 零售价格
     */
    @TableField("hdcost")
    private BigDecimal retailPrice;

    /**
     * 上架时间
     */
    @TableField("requireDate")
    private String registerDate;


}
