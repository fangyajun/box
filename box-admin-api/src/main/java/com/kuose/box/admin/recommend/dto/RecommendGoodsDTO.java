package com.kuose.box.admin.recommend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/11/6
 */
@Data
public class RecommendGoodsDTO {

    @ApiModelProperty(value="搭配单id")
    private Integer boxRecommendId;

    @ApiModelProperty(value="搭配师名称")
    private String username;

    @ApiModelProperty(value="搭配推荐文案")
    private String coordinatorMessage;

    @ApiModelProperty(value="数组，skuId")
    private Integer[] skuIds;
}
