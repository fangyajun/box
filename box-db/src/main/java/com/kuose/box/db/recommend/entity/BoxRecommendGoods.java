package com.kuose.box.db.recommend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 用户推荐商品商品表
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Data
public class BoxRecommendGoods extends Model<BoxRecommendGoods> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 推荐表的id
     */
    @ApiModelProperty(value="推荐表的id")
    private Integer boxRecommendId;

    /**
     * 商品表的商品ID
     */
    @ApiModelProperty(value="商品表的商品ID")
    private Integer goodsId;

    /**
     * 商品表SKU的ID
     */
    @ApiModelProperty(value="商品表SKU的ID")
    private Integer skuId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value="商品名称")
    private String goodsName;

    /**
     * 商品编号
     */
    @ApiModelProperty(value="商品编号")
    private String goodsNo;

    /**
     * sku编号
     */
    @ApiModelProperty(value="sku编号")
    private String skuNo;

    /**
     * 商品sku的售价
     */
    @ApiModelProperty(value="商品sku的售价")
    private BigDecimal price;

    /**
     * 颜色名称
     */
    @ApiModelProperty(value="颜色名称")
    private String colorName;

    /**
     * 尺寸名称
     */
    @ApiModelProperty(value="尺寸名称")
    private String sizeName;

    /**
     * 商品货品图片或者商品图片
     */
    @ApiModelProperty(value="商品货品图片或者商品图片")
    private String picUrl;

    /**
     * 商品天猫链接
     */
    @ApiModelProperty(value="商品天猫链接")
    private String tmallUrl;

    /**
     * 淘口令链接
     */
    @ApiModelProperty(value="淘口令链接")
    private String taoUrl;

    /**
     * 创建时间 
     */
    @ApiModelProperty(value="逻辑删除", hidden = true)
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="逻辑删除", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value="逻辑删除", hidden = true)
    private Integer deleted;

}
