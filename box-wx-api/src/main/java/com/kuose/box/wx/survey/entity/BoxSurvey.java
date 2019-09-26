package com.kuose.box.wx.survey.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 问卷
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-24
 */
@Data
public class BoxSurvey extends Model<BoxSurvey> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问卷名称
     */
    private String surveyName;

    /**
     * 问卷描述
     */
    private String description;

    /**
     * 问卷描述
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    private Integer isDeleted;

}
