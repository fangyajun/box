package com.kuose.box.admin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @ApiModelProperty(value="商品分类编码")
    private String goodsNo;

    /**
     * sku编码
     */
    @ApiModelProperty(value="商品分类编码")
    private String skuCode;

    /**
     * 商品规格值列表，采用JSON数组格式
     */
    @ApiModelProperty(value="商品规格值列表", example = "白色,S")
    private String specifications;

    /**
     * 颜色名称
     */
    @ApiModelProperty(value="颜色名称")
    private String colorName;

    /**
     * 颜色编码
     */
    @ApiModelProperty(value="颜色编码")
    private String colorCode;

    /**
     * 尺寸名称
     */
    @ApiModelProperty(value="尺寸名称")
    private String sizeName;

    /**
     * 尺寸编码
     */
    @ApiModelProperty(value="尺寸编码")
    private String sizeCode;

    /**
     * 专柜价格
     */
    @ApiModelProperty(value="专柜价格")
    private BigDecimal counterPrice;

    /**
     * 吊牌价格
     */
    @ApiModelProperty(value="吊牌价格")
    private BigDecimal tagPrice;

    /**
     * 零售价格
     */
    @ApiModelProperty(value="零售价格")
    private BigDecimal retailPrice;

    /**
     * 商品货品数量，库存
     */
    @ApiModelProperty(value="商品货品数量，库存")
    private Integer number;

    /**
     * 状态,0:正常状态
     */
    @ApiModelProperty(value="状态,0:正常状态")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(hidden = true)
    private Integer deleted;

}
