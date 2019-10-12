package com.kuose.box.admin.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/12
 */
@Data
public class ShipDTO {

    private Integer orderId;
    @ApiModelProperty(value="快递公司编号")
    private String shipChannel;
    @ApiModelProperty(value="订单编号")
    private String shipSn;
    @ApiModelProperty(value="发货时间")
    private Long shipTime;
}
