package com.kuose.box.db.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单商品评论表
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-10-07
 */
@Data
public class BoxOrderGoodsComment extends Model<BoxOrderGoodsComment> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单表的订单ID
     */
    @ApiModelProperty(value="订单表的订单ID")
    private Integer orderId;

    /**
     * 商品表的商品skuId
     */
    @ApiModelProperty(value="商品表的商品skuId")
    private Integer skuId;

    /**
     * 用户表的用户ID
     */
    @ApiModelProperty(value="用户表的用户ID")
    private Integer userId;

    /**
     * 商品是否保留，0:默认状态，1：保留，2：退货，3：换货
     */
    @ApiModelProperty(value="商品是否保留，0:默认状态，1：保留，2：退货，3：换货")
    private Integer orderGoodsStatus;

    /**
     * 尺码评价，0:默认，未评价，1：太小，2：合适，3：太大
     */
    @TableField("`size`")
    @ApiModelProperty(value="尺码评价，0:默认，未评价，1：太小，2：合适，3：太大")
    private Integer size;

    /**
     * 款式评价，0:默认，未评价，1：不喜欢，2：一般，3：喜欢，4：非常喜欢
     */
    @ApiModelProperty(value="款式评价，0:默认，未评价，1：不喜欢，2：一般，3：喜欢，4：非常喜欢")
    private Integer style;

    /**
     * 合适度评价，0:默认，未评价，1：不合适，2：一般，3：合适，4：很合适
     */
    @TableField("`match`")
    @ApiModelProperty(value="合适度评价，0:默认，未评价，1：不合适，2：一般，3：合适，4：很合适")
    private Integer match;

    /**
     * 质量评价，0:默认，未评价，1：差，2：一般，3：好，4：很好
     */
    @ApiModelProperty(value="质量评价，0:默认，未评价，1：差，2：一般，3：好，4：很好")
    private Integer quality;

    /**
     * 价格评价，0:默认，未评价，1：太高，2：一般，3：合适，4：便宜
     */
    @ApiModelProperty(value="价格评价，0:默认，未评价，1：太高，2：一般，3：合适，4：便宜")
    private Integer price;

    /**
     * 评论内容
     */
    @ApiModelProperty(value="评论内容")
    private String content;

    /**
     * 评分， 1-5
     */
    @ApiModelProperty(value="评分， 1-5")
    private Integer star;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间", hidden = true)
    private Long addTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value="逻辑删除", hidden = true)
    private Integer deleted;

}
