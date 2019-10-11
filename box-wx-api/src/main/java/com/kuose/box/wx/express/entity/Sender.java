package com.kuose.box.wx.express.entity;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/10
 */

import lombok.Data;

/**
 * 发件人
 */
@Data
public class Sender {

    /**
     * 发件人
     */
    private String Name;
    /**
     * 手机
     */
    private String Mobile;
    /**
     * 	发件省（如广东省，不要缺少“省”）
     */
    private String ProvinceName;
    /**
     * 	发件市（如深圳市，不要缺少“市”）
     */
    private String CityName;
    /**
     * 发件区（如福田区，不要缺少“区”或“县”）
     */
    private String ExpAreaName;
    /**
     * 发件人详细地址
     */
    private String Address;


}
