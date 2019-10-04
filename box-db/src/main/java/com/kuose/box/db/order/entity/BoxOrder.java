package com.kuose.box.db.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Data
public class BoxOrder extends Model<BoxOrder> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表的用户ID
     */
    private Integer userId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单状态,0-待搭配状态，1-已搭配未发货，2-已发货未签收，3-已签收未确认收货，4-已签收确认收货，5-支付, 7-已关闭
     */
    private Integer orderStatus;

    /**
     * 审核状态,0-未审核，1-已审核,审核通过，2-审核未通
     */
    private Integer auditStatus;

    /**
     * 收货地址表的ID
     */
    private Integer addrId;

    /**
     * 微信昵称
     */
    private String nicName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户订单留言
     */
    private String userMessage;

    /**
     * 搭配师留言
     */
    private String coordinatorMessage;


    /**
     * 商品总费用
     */
    private BigDecimal goodsPrice;

    /**
     * 优惠券减免
     */
    private BigDecimal couponPrice;

    /**
     * 用户积分减免
     */
    private BigDecimal integralPrice;

    /**
     * 预付款
     */
    private BigDecimal advancePrice;

    /**
     * 订单费用， = goods_price - coupon_price
     */
    private BigDecimal orderPrice;

    /**
     * 实付费用， = order_price - integral_price
     */
    private BigDecimal actualPrice;

    /**
     * 微信付款编号
     */
    private String payId;

    /**
     * 微信付款时间
     */
    private Long payTime;

    /**
     * 发货编号
     */
    private String shipSn;

    /**
     * 发货快递公司
     */
    private String shipChannel;

    /**
     * 发货开始时间
     */
    private Long shipTime;

    /**
     * 用户设定收盒时间
     */
    private Long expectTime;

    /**
     * 用户确认收盒时间
     */
    private Long confirmTime;

    /**
     * 待评价订单商品数量
     */
    private Integer comments;

    /**
     * 搭配师
     */
    private String coordinator;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;

}
