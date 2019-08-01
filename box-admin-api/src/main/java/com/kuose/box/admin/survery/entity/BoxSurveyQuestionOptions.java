package com.kuose.box.admin.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 问题选项表
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Getter
@Setter
public class BoxSurveyQuestionOptions extends Model<BoxSurveyQuestionOptions> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题的id
     */
    private Integer questionId;

    /**
     * 选项的内容
     */
    private String optionContent;

    /**
     * 选项的图片
     */
    private String optionPic;

    /**
     * 选项排序,数越大排序靠前
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

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
