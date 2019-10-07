package com.kuose.box.admin.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/5
 */
@Data
public class OrderGoodsDto {

    @ApiModelProperty(value="订单id")
    private Integer orderId;

    @ApiModelProperty(value="搭配师名称")
    private String username;

    @ApiModelProperty(value="搭配师给用户留言")
    private String coordinatorMessage;

    @ApiModelProperty(value="数组，skuId")
    private Integer[] skuIds;
}
