package com.kuose.box.wx.express.dto;

import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/15
 */
@Data
public class AppointmentExpressDTO {

    private Integer orderId;
    private Integer addrId;
    /**
     * 上门取货开始时间:"yyyy-MM-dd HH:mm:ss"格式化，本文中所有时间格式相同
     */
    private Long StartDate;
    /**
     * 上门取货结束时间:"yyyy-MM-dd HH:mm:ss"格式化，本文中所有时间格式相同
     */
    private Long EndDate;
}
