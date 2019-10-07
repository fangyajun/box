package com.kuose.box.admin.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/7
 */
@Data
public class OrderAuditDto {

    @ApiModelProperty(value="订单id")
    private Integer orderId;

    @ApiModelProperty(value="审核人名称")
    private String username;

    @ApiModelProperty(value="审核状态，1：审核通过，2：审核未通过")
    private Integer status;
}
