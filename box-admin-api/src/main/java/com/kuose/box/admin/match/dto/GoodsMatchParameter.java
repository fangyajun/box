package com.kuose.box.admin.match.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/9
 */
@Data
public class GoodsMatchParameter {

    @ApiModelProperty(value="查询条件，用户id")
    private Integer userId;

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

    @ApiModelProperty(value="颜色名称")
    private String colorName;

    @ApiModelProperty(value="颜色编码")
    private String colorCode;

    @ApiModelProperty(value="尺寸编码")
    private String sizeCode;



    @ApiModelProperty(value="避免的颜色，多个用数组表示", hidden = true)
    private String[] avoidColor;

    @ApiModelProperty(value="避免的材质，多个用数组表示", hidden = true)
    private String[] avoidTexture;

    @ApiModelProperty(value="避免的类别，多个用数组表示", hidden = true)
    private String[] avoidCategory;

    @ApiModelProperty(value="避免的花纹图案，多个用数组表示", hidden = true)
    private String[] avoidFigure;



    @ApiModelProperty(value="上装尺寸", hidden = true)
    private String topSize;

    @ApiModelProperty(value="连衣裙尺寸", hidden = true)
    private String dressSize;

    @ApiModelProperty(value="下装尺寸", hidden = true)
    private String bottomsSize;

    @ApiModelProperty(value="牛仔裤尺寸", hidden = true)
    private String jeansSize;


    @ApiModelProperty(value="上装最低价格", hidden = true)
    private Double topLowPrice;

    @ApiModelProperty(value="上装最高价格", hidden = true)
    private Double topHighPrice;

    @ApiModelProperty(value="下装最低价格", hidden = true)
    private Double bottomsLowPrice;

    @ApiModelProperty(value="下装最高价格", hidden = true)
    private Double bottomsHighPrice;

    @ApiModelProperty(value="连衣裙最低价格", hidden = true)
    private Double dressLowPrice;

    @ApiModelProperty(value="连衣裙最高价格", hidden = true)
    private Double dressHighPrice;

    @ApiModelProperty(value="外套最低价格", hidden = true)
    private Double overcoatLowPrice;

    @ApiModelProperty(value="外套最高价格", hidden = true)
    private Double overcoatHighPrice;

}
