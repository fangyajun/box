package com.kuose.box.wx.test;

import com.kuose.box.wx.common.service.UserTokenManager;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/25
 */
public class Mytest {


    public static void main(String[] args) {
        String s = UserTokenManager.generateToken(1);
        System.out.println("s = " + s);
    }
}
