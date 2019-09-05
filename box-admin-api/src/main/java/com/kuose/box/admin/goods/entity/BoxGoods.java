package com.kuose.box.admin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
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
public class BoxGoods extends Model<BoxGoods> {

private static final long serialVersionUID=1L;

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
    @TableField("`name`")
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
     * 商品宣传图片列表，采用JSON数组格式
     */
    @ApiModelProperty(value="商品宣传图片列表，采用JSON数组格式")
    private String img;

    /**
     * 商品天猫链接
     */
    @ApiModelProperty(value="商品天猫链接")
    private String tmallUrl;

    /**
     * 是否上架
     */
    @ApiModelProperty(value="是否上架")
    private Integer isOnSale;

    /**
     * 季节
     */
    @ApiModelProperty(value="季节")
    private String quarter;

    /**
     * 年份
     */
    @TableField("`year`")
    @ApiModelProperty(value="年份", example = "2")
    private String year;

    /**
     * 风貌编号，多个用逗号隔开
     */
    @ApiModelProperty(value="风貌编号，多个用逗号隔开")
    private String featuresStyleNos;

    /**
     * 搭配款货号，多个用逗号隔开
     */
    @ApiModelProperty(value="搭配款货号，多个用逗号隔开")
    private String matchStyleNos;

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
     * 商品简介、备注
     */
    @ApiModelProperty(value="商品简介、备注")
    private String remark;

    /**
     * 上架时间
     */
    @ApiModelProperty(value="上架时间")
    private Long registerDate;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(hidden = true)
    private Integer deleted;

}
