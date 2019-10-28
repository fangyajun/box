package com.kuose.box.wx.express.entity;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/10
 */

import lombok.Data;

import java.util.List;

/**
 * 预约快递信息
 */
@Data
public class AppointmentExpressInfo {
    /**
     *快递公司编码
     */
    private String ShipperCode;
    /**
     *订单编号
     */
    private String OrderCode;
    /**
     *邮费支付方式:1-现付，2-到付，3-月结，4-第三方支付
     */
    private Integer PayType = 2;

    /**
     *发货方式：0-上门揽件，1-网点自寄
     */
    private Integer IsNotice = 0;
    /**
     *快递类型：1-标准快件
     */
    private String ExpType = "1";
    /**
     *寄件者
     */
    private Sender Sender;
    /**
     *收件zhe
     */
    private Receiver Receiver;

    /**
     * 上门取货开始时间:"yyyy-MM-dd HH:mm:ss"格式化，本文中所有时间格式相同
     */
    private String StartDate;
    /**
     * 上门取货结束时间:"yyyy-MM-dd HH:mm:ss"格式化，本文中所有时间格式相同
     */
    private String EndDate;

    /**
     *商品信息
     */
    private List<Commodity> Commodity;

    /**
     *备注
     */
    private String Remark;

}
