package com.kuose.box.admin.algorithm.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author fangyajun
 * @description
 * @since 2019/11/22
 */
@Data
public class RecommendGoods {

    @ApiModelProperty(value="商品表的商品ID")
    private Integer goodsId;

    @ApiModelProperty(value="商品sku表的ID")
    private Integer skuId;

    @ApiModelProperty(value="商品名称")
    private String goodsName;

    @ApiModelProperty(value="商品类别")
    private String categoryCode;

    @ApiModelProperty(value="商品编号")
    private String goodsNo;

    @ApiModelProperty(value="季节")
    private String quarter;

    @ApiModelProperty(value="sku编号")
    private String skuNo;

    @ApiModelProperty(value="商品sku的售价")
    private BigDecimal price;

    @ApiModelProperty(value="颜色名称")
    private String colorName;

    @ApiModelProperty(value="尺寸名称")
    private String sizeName;

    @ApiModelProperty(value="天猫链接")
    private String tmallUrl;

    @ApiModelProperty(value="商品货品图片或者商品图片")
    private String picUrl;

    @ApiModelProperty(value="不是精修图")
    private String oldPicUrl;

    @ApiModelProperty(value="sku图片，暂时和商品图片一致，没有颜色图片")
    private String skuUrl;

}
