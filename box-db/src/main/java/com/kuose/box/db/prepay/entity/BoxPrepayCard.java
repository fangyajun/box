package com.kuose.box.db.prepay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 预付金或服务卡表
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Data
@TableName(value = "box_prepay_card", resultMap = "BaseResultMap")
public class BoxPrepayCard extends Model<BoxPrepayCard> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 预付的名称，如：￥99预付金
     */
    @ApiModelProperty(value = "预付的名称，如：￥99预付金", example = "￥99预付金")
    private String prepayName;

    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 实际价格
     */
    @ApiModelProperty(value = "实际价格", example = "99.00")
    private BigDecimal retailPrice;

    /**
     * 吊牌价格
     */
    @ApiModelProperty(value = "吊牌价格", example = "199.00")
    private BigDecimal tagPrice;

    /**
     * 类别，0：预付金，1：服务卡
     */
    @ApiModelProperty(value = "类别，0：预付金，1：服务卡", example = "0")
    private Integer category;

    /**
     * 服务次数，-1：表示预付金，0：无限次，1:1次，2:2次，3:3次
     */
    @ApiModelProperty(value = "服务次数，-1：表示预付金，0：无限次，1:1次，2:2次，3:3次", example = "-1")
    private Integer serviceTimes;

    /**
     * 是否限制，0：不限制，1：限制
     */
    @ApiModelProperty(value = "是否限制，0：不限制，1：限制", example = "0")
    private Integer isRestrict;

    /**
     * 限制人群的标签code，多个用数组表示
     */
    @ApiModelProperty(value = "限制人群的标签code，多个用数组表示", example = "[]")
    @TableField(el = "userLabelCode,jdbcType=VARCHAR,typeHandler=com.kuose.box.db.mybatis.JsonStringArrayTypeHandler")
    private String[] userLabelCode;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片", example = "img")
    private String prepayCardImg;

    /**
     * 状态,0:未启用，1:启用
     */
    @ApiModelProperty(value = "状态,0:未启用，1:启用", example = "0")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Long addTime;

    /**
     *
     */
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除", hidden = true)
    private Integer deleted;

}
