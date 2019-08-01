package com.kuose.box.admin.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
    private Integer userId;

    /**
     * 问题id
     */
    private Integer questionId;

    /**
     * 选项id
     */
    private Integer optionId;

    /**
     * 选项内容,填空填写
     */
    private String optionContent;

    /**
     * 填写时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}
