package com.kuose.box.wx.express.entity;

import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/10
 */
@Data
public class Commodity {
    /**
     * 商品名称
     */
    private String GoodsName;
    /**
     * 商品数量
     */
    private Integer Goodsquantity;

}
