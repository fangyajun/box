package com.kuose.box.db.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单评论表
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-10-07
 */
@Data
public class BoxOrderComment extends Model<BoxOrderComment> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单表的订单ID
     */
    @ApiModelProperty(value="订单表的订单ID")
    private Integer orderId;

    /**
     * 用户表的用户ID
     */
    @ApiModelProperty(value="用户表的用户ID")
    private Integer userId;

    /**
     * 量身定制评分，1-5
     */
    @ApiModelProperty(value="量身定制评分，1-5")
    private Integer customize;

    /**
     * 风格评分，1-5
     */
    @ApiModelProperty(value="风格评分，1-5")
    private Integer style;

    /**
     * 服务评分，1-5
     */
    @ApiModelProperty(value="服务评分，1-5")
    private Integer serve;

    /**
     * 是否期待收到下一个盒子，0：默认未填写，1：是很期待，2：不，不期待
     */
    @ApiModelProperty(value="是否期待收到下一个盒子，0：默认未填写，1：是很期待，2：不，不期待")
    private Integer nextBox;

    /**
     * 是否继续用这个搭配师，0：默认未填写，1；是，1：不，3：都可以
     */
    @ApiModelProperty(value="是否继续用这个搭配师，0：默认未填写，1；是，1：不，3：都可以")
    private Integer coordinator;

    /**
     * 评论内容
     */
    @ApiModelProperty(value="评论内容")
    private String content;

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
    private Integer deleted;


}
