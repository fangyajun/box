package com.kuose.box.admin.goods.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fangyajun
 * @description
 * @since 2019/8/26
 */
@Getter
@Setter
public class GoodsQueryParameter {

    @ApiModelProperty(value="查询条件，商品所属类目code")
    private String categoryCode;

    @ApiModelProperty(value="查询条件，商品编号/货号")
    private String goodsNo;

    @ApiModelProperty(value="商品名称")
    private String goodsName;

    @ApiModelProperty(value="季节")
    private String quarter;

    @ApiModelProperty(value="年份")
    private Integer year;

    @ApiModelProperty(value="最低价格")
    private Double lowPrice;

    @ApiModelProperty(value="最高价格")
    private Double highPrice;

    @ApiModelProperty(value="属性编号（数组）")
    private String[] goodsAttributeCodes;

    @ApiModelProperty(value="避免的属性编号（数组）")
    private String[] avoidGoodsAttributeCodes;

    @ApiModelProperty(value="避免的颜色编号（数组）")
    private String[] avoidGoodsColorCodes;

    @ApiModelProperty(value="颜色名称")
    private String colorName;

    @ApiModelProperty(value="颜色编码")
    private String colorCode;

    @ApiModelProperty(value="尺寸编码")
    private String sizeCode;



}
