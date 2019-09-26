package com.kuose.box.wx.survey.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 问题表
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-24
 */
@Data
public class BoxSurveyQusetion extends Model<BoxSurveyQusetion> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id，修改和删除时必传")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题类型，1单选，2多选，3填空
     */
    @ApiModelProperty(value = "问题类型，1单选，2多选，3填空", example = "2")
    private Integer questionType;

    /**
     * 问题标签编号
     */
    @ApiModelProperty(value = "问题标签编号", example = "XZCJ")
    private String labelCode;

    /**
     * 问题主题内容
     */
    @ApiModelProperty(value = "问题主题内容", example = "下半身你偏向于哪种裁剪？")
    private String questionTopic;

    /**
     * 问题内容图片
     */
    @ApiModelProperty(value = "问题内容图片")
    private String questionPic;

    /**
     * 问卷id
     */
    @ApiModelProperty(value = "问卷id", example = "1")
    private Integer surveyId;

    /**
     * 排序，数字越大，排序越靠前
     */
    @ApiModelProperty(value = "排序，数字越大，排序越靠前", example = "10")
    private Integer sort;

    /**
     * 是否必填，0 否，1 是
     */
    @ApiModelProperty(value = "是否必填，0 否，1 是", example = "1")
    @TableField("is_qequired")
    private Integer qequired;

    /**
     * 说明，描述
     */
    @ApiModelProperty(value = "说明，描述")
    private String description;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Long createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除（1.删除，0正常）
     */
    @ApiModelProperty(value = "逻辑删除", hidden = true)
    @TableField("is_deleted")
    private Integer deleted;

    @TableField(exist = false)
    private List<BoxSurveyQuestionOptions> optionsList;

    @TableField(exist = false)
    private List<BoxSurveyUserAnswer> userAnswerList;

}
