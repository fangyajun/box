package com.kuose.box.db.recommend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * box_user_recommend
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Data
public class BoxRecommend extends Model<BoxRecommend> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表的用户ID
     */
    @ApiModelProperty(value="用户表的用户ID")
    private Integer userId;

    /**
     * 用户推荐表的id
     */
    @ApiModelProperty(value="用户推荐表的id")
    private Integer userRecommendId;

    /**
     * 推荐留言
     */
    @ApiModelProperty(value="推荐留言")
    private String recommendMessage;

    /**
     * 搭配师
     */
    @ApiModelProperty(value="搭配师")
    private String coordinator;

    /**
     * 审核状态,0-未审核，1-已审核,审核通过，2-审核未通
     */
    @ApiModelProperty(value="审核状态,0-未审核，1-已审核,审核通过，2-审核未通")
    private Integer auditStatus;

    /**
     * 评价状态，0：未评价，1：已评价
     */
    @ApiModelProperty(value="评价状态，0：未评价，1：已评价")
    private Integer commentStatus;

    /**
     * 排序，数字越大，排序越靠前
     */
    @ApiModelProperty(value="排序，数字越大，排序越靠前")
    private Integer sort;

    /**
     * 创建时间 
     */
    @ApiModelProperty(value="创建时间", hidden = true)
    private Long createTime;

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

    @TableField(exist = false)
    private List<BoxRecommendGoods> boxRecommendGoodsList;


}
