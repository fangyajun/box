package com.kuose.source.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 商品SKU表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Data
public class BoxGoodsSku extends Model<BoxGoodsSku> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品表的商品ID
     */
    private Integer goodsId;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 商品规格值列表，采用JSON数组格式
     */
    private String specifications;

    /**
     * 颜色名称
     */
    private String colorName;

    /**
     * 颜色编码
     */
    private String colorCode;

    /**
     * 尺寸名称
     */
    private String sizeName;

    /**
     * 尺寸编码
     */
    private String sizeCode;

    /**
     * 专柜价格
     */
    private BigDecimal counterPrice;

    /**
     * 吊牌价格
     */
    private BigDecimal tagPrice;

    /**
     * 零售价格
     */
    private BigDecimal retailPrice;

    /**
     * 商品货品数量，库存
     */
    private Integer number;

    /**
     * sku对应的图片
     */
    private String skuImg;


}
