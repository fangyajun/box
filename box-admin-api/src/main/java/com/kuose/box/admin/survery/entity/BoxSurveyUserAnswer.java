package com.kuose.box.admin.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 问卷用户答案表
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Data
public class BoxSurveyUserAnswer extends Model<BoxSurveyUserAnswer> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", example = "10")
    private Integer userId;

    /**
     * 问题id
     */
    @ApiModelProperty(value = "问题id", example = "1")
    private Integer questionId;


    /**
     * 选项id数组，
     */
    @ApiModelProperty(value = "选项id数组", example = "[2,3,4]")
    @TableField(el = "optionIds,jdbcType=VARCHAR,typeHandler=com.kuose.box.admin.mybatis.JsonIntegerArrayTypeHandler")
    private Integer[] optionIds;

    /**
     * 问题标签编号
     */
    @ApiModelProperty(value = "问题标签编号", example = "XZCJ")
    private String labelCode;

    /**
     * 问题答案值,数组
     */
    @ApiModelProperty(value = "问题答案值,数组", example = "[\"贴身裁剪\",\"合身裁剪\",\"直筒裁剪\"]")
    @TableField(el = "optionValues,jdbcType=VARCHAR,typeHandler=com.kuose.box.admin.mybatis.JsonStringArrayTypeHandler")
    private String[] optionValues;

    /**
     * 选项内容,填空填写
     */
    @ApiModelProperty(value = "选项内容,填空填写,格式：[{:},{:}]", example = "[{\"SG\":\"165cm\"},{\"TZ\":\"50kg\"}]")
    private String optionContent;

    /**
     * 填写时间
     */
    @ApiModelProperty(value = "填写时间", hidden = true)
    private Long createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Long updateTime;
}
