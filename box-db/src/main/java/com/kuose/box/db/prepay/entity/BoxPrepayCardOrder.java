package com.kuose.box.db.prepay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 预付金或服务卡订单表
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Data
public class BoxPrepayCardOrder extends Model<BoxPrepayCardOrder> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表的用户ID
     */
    @ApiModelProperty(value = "用户表的用户ID", example = "1")
    private Integer userId;

    /**
     * 预付金或服务卡表ID
     */
    @ApiModelProperty(value = "预付金或服务卡表ID", example = "1")
    private Integer prepayCardId;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 订单类别，0：预付金，1：服务卡
     */
    @ApiModelProperty(value = "订单类别，0：预付金，1：服务卡")
    private Integer category;

    /**
     * 订单状态,0-已提交未支付，1-已调用微信支付但未支付，2-已支付但未服务，3-服务中，4-，5-已完成，服务次数已用完，预付金已用完
     */
    @ApiModelProperty(value = "订单状态,订单状态,0-已提交未支付，1-已调用微信支付但未支付，2-已支付但未服务，3-服务中，4-，5-已完成，服务次数已用完，预付金已用完")
    private Integer orderStatus;

    /**
     * 服务次数，-1：表示预付金，-2：0次，0：无限次，1:1次，2:2次，3:3次'
     */
    @ApiModelProperty(value = "服务次数，-1：表示预付金，-2：0次，0：无限次，1:1次，2:2次，3:3次'")
    private Integer serviceTimes;

    /**
     * 预付金可用的金额
     */
    @ApiModelProperty(value = "预付金可用的金额")
    private BigDecimal vailableAmount;

    /**
     * 总费用
     */
    @ApiModelProperty(value = "总费用")
    private BigDecimal prepayPrice;

    /**
     * 优惠券减免
     */
    @ApiModelProperty(value = "优惠券减免")
    private BigDecimal couponPrice;

    /**
     * 订单费用， = prepay_price - coupon_price
     */
    @ApiModelProperty(value = "订单费用， = prepay_price - coupon_price")
    private BigDecimal orderPrice;

    /**
     * 预付金是否可退 0：不可退，1：可退， 2:已退
     */
    @TableField("is_refund")
    @ApiModelProperty(value = "预付金是否可退 0：不可退，1：可退, 2:已退")
    private Integer refund;

    /**
     * 预付金可退款的金额
     */
    @ApiModelProperty(value="预付金退款的金额")
    private BigDecimal refundPrepayAmounts;

    /**
     * 微信付款编号
     */
    @ApiModelProperty(value = "微信付款编号")
    private String payId;

    /**
     * 微信付款时间
     */
    @ApiModelProperty(value = "微信付款时间")
    private Long payTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Long addTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除", hidden = true)
    private Integer deleted;

}
