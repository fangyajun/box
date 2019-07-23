package com.kuose.box.wx.common.notify;

public enum NotifyType {
    PAY_SUCCEED("paySucceed"),
    SHIP("ship"),
    REFUND("refund"),
    CAPTCHA("captcha");

    private String type;

    NotifyType(String type) {
        this.type = type;
}

    public String getType() {
        return this.type;
    }
}
