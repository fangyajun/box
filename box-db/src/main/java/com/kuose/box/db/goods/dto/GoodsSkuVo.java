package com.kuose.box.db.goods.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/5
 */
@Data
public class GoodsSkuVo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号/货号
     */
    @ApiModelProperty(value="商品编号/货号")
    private String goodsNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value="商品名称")
    private String name;

    /**
     * 商品品牌
     */
    @ApiModelProperty(value="商品品牌")
    private String brand;

    /**
     * 商品所属类目ID
     */
    @ApiModelProperty(value="商品所属类目ID")
    private String categoryCode;

    /**
     * 商品宣传图片列表
     */
    @ApiModelProperty(value="商品宣传图片列表，采用JSON数组格式")
    private String img;

    /**
     * 商品天猫链接
     */
    @ApiModelProperty(value="商品天猫链接")
    private String tmallUrl;

    /**
     * 季节
     */
    @ApiModelProperty(value="季节")
    private String quarter;

    /**
     * 年份
     */
    @ApiModelProperty(value="年份", example = "2")
    private Integer year;

    /**
     * 零售价格
     */
    @ApiModelProperty(value="零售价格")
    private BigDecimal retailPrice;

    @ApiModelProperty(value="零售价格", hidden = true)
    private String skuIds;


    @ApiModelProperty(value="商品的sku集合")
    private List<BoxGoodsSku> boxGoodsSkuList;
}
