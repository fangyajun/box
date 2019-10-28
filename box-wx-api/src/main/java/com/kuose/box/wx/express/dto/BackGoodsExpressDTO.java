package com.kuose.box.wx.express.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/28
 */
@Data
public class BackGoodsExpressDTO {

    @ApiModelProperty(value="订单表的订单ID")
    private Integer orderId;

    @ApiModelProperty(value="寄回物流公司编号")
    private String backShipChannel;

    @ApiModelProperty(value="寄回快递单号")
    private String backShipSn;
}
