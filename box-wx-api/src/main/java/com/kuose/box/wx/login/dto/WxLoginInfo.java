package com.kuose.box.wx.login.dto;

import lombok.Data;

@Data
public class WxLoginInfo {
    private String code;
    private UserInfo userInfo;

}
