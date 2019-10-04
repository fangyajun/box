package com.kuose.box.admin.order.dto;

import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/4
 */
@Data
public class OrderDto {

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
     * 手机号码
     */
    private String mobile;

    /**
     * 用户订单留言
     */
    private String userMessage;


    /**
     * 设定最小收盒时间
     */
    private Long minExpectTime;

    /**
     *设定最大收盒时间
     */
    private Long maxExpectTime;


}
