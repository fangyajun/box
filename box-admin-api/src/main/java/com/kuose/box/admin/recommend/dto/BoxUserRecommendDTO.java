package com.kuose.box.admin.recommend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/11/6
 */
@Data
public class BoxUserRecommendDTO {

    @ApiModelProperty(value="推荐状态,0-未推荐，1-推荐中，2-已推荐")
    private Integer recommendStatus;

}





