package com.kuose.box.wx.express.entity;

import lombok.Data;

/**
 * @author fangyajun
 * @description  收件
 * @since 2019/10/10
 */
@Data
public class Receiver {

    /**
     * 发件人
     */
    private String Name;
    /**
     * 手机
     */
    private String Mobile;
    /**
     * 收件省（如广东省，不要缺少“省”）
     */
    private String ProvinceName;
    /**
     * 收件市（如深圳市，不要缺少“市”）
     */
    private String CityName;
    /**
     * 	收件区（如福田区，不要缺少“区”或“县”）
     */
    private String ExpAreaName;
    /**
     * 收件人详细地址
     */
    private String Address;
}
