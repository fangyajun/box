package com.kuose.box.db.recommend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.kuose.box.db.user.entity.BoxUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 用户推荐表
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Data
public class BoxUserRecommend extends Model<BoxUserRecommend> {

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
     * 推荐状态,0-未推荐，1-推荐中，2-已推荐， 3-已完成（评价完变成此状态）
     */
    @ApiModelProperty(value="推荐状态,0-未推荐，1-推荐中，2-已推荐， 3-已完成（评价完变成此状态）")
    private Integer recommendStatus;

    /**
     * 推荐搭配的套数
     */
    @ApiModelProperty(value="推荐搭配的套数")
    private Integer recommendNumber;

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
    private BoxUser boxUser;

    @TableField(exist = false)
    private List<BoxRecommend> boxRecommendList;

}
