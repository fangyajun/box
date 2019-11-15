package com.kuose.box.db.recommend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * <p>
 * 推荐商品评价
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-15
 */
@Data
public class BoxRecommendGoodsComment extends Model<BoxRecommendGoodsComment> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表的用户ID
     */
    @ApiModelProperty(value="用户表的用户ID")
    private Integer userId;

    /**
     * 推荐单id
     */
    @ApiModelProperty(value="推荐商品表的id")
    private Integer boxRecommendGoodsId;

    /**
     * 商品表的商品ID
     */
    @ApiModelProperty(value="商品表的商品ID")
    private Integer goodsId;

    /**
     * 商品表sku的skuID
     */
    @ApiModelProperty(value="商品表sku的skuID")
    private Integer skuId;

    /**
     * 尺码评价，0:默认，未评价，1：太小，2：合适，3：太大
     */
    @ApiModelProperty(value="尺码评价，0:默认，未评价，1：太小，2：合适，3：太大")
    private Integer size;

    /**
     * 款式评价，0:默认，未评价，1：不喜欢，2：一般，3：喜欢，4：非常喜欢
     */
    @ApiModelProperty(value="款式评价，0:默认，未评价，1：不喜欢，2：一般，3：喜欢，4：非常喜欢")
    private Integer style;

    /**
     * 合适度评价，0:默认，未评价，1：不合适，2：一般，3：合适，4：很合适
     */
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

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间", hidden = true)
    private Long addTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value="逻辑删除", hidden = true)
    private In deleted;

}
