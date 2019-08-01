package com.kuose.box.admin.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 问题表
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Data
public class BoxSurveyQusetion extends Model<BoxSurveyQusetion> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题类型，1单选，2多选，3填空
     */
    private Integer questionType;

    /**
     * 问题主题内容
     */
    private String questionTopic;

    /**
     * 问题内容图片
     */
    private String questionPic;

    /**
     * 问卷id
     */
    private Integer surveyId;

    /**
     * 排序，数字越大，排序越靠前
     */
    private Integer sort;

    /**
     * 是否必填，0 否，1 是
     */
    private Integer isQequired;

    /**
     * 说明，描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 逻辑删除（1.删除，0正常）
     */
    @TableField("is_deleted")
    private Integer deleted;
}
