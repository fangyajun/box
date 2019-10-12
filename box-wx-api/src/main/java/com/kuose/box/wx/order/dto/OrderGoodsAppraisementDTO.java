package com.kuose.box.wx.order.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/12
 */
@Data
public class OrderGoodsAppraisementDTO {

    @ApiModelProperty(value="订单商品表的id")
    private Integer orderGoodsId;

    private Integer userId;

    /**
     * 商品是否保留，0:默认状态，1：保留，2：退货，3：换货
     */
    @ApiModelProperty(value="商品是否保留，0:默认状态，1：保留，2：退货，3：换货")
    private Integer orderGoodsStatus;

    /**
     * 尺码评价，0:默认，未评价，1：太小，2：合适，3：太大
     */
    @TableField("`size`")
    @ApiModelProperty(value="尺码评价，0:默认，未评价，1：太小，2：合适，3：太大")
    private Integer size;


    @ApiModelProperty(value="款式评价，0:默认，未评价，1：不喜欢，2：一般，3：喜欢，4：非常喜欢")
    private Integer style;

    /**
     * 合适度评价，0:默认，未评价，1：不合适，2：一般，3：合适，4：很合适
     */
    @TableField("`match`")
    @ApiModelProperty(value="合适度评价，0:默认，未评价，1：不合适，2：一般，3：合适，4：很合适")
    private Integer match;

    /**
     * 质量评价，0:默认，未评价，1：差，2：一般，3：好，4：很好
     */
    @ApiModelProperty(value="质量评价，0:默认，未评价，1：差，2：一般，3：好，4：很好")
    private Integer quality;

    /**
     * 价格评价，0:默认，未评价，1：太高，2：一般，3：合适，4：便宜
     */
    @ApiModelProperty(value="价格评价，0:默认，未评价，1：太高，2：一般，3：合适，4：便宜")
    private Integer price;

    /**
     * 评论内容
     */
    @ApiModelProperty(value="评论内容")
    private String content;

    /**
     * 评分， 1-5
     */
    @ApiModelProperty(value="评分， 1-5")
    private Integer star;


}
