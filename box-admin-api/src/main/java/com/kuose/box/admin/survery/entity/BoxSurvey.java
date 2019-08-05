package com.kuose.box.admin.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 问卷
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Getter
@Setter
public class BoxSurvey extends Model<BoxSurvey> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id，添加不需要传入,编辑删除需要传入")
    private Integer id;

    /**
     * 问卷名称
     */
    @ApiModelProperty(value = "问卷名称", example = "穿衣风格")
    private String surveyName;

    /**
     * 问卷描述
     */
    @ApiModelProperty(value = "问卷描述", example = "穿衣风格调查问券描述")
    private String description;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序,数字越大排名靠前", example = "1")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间，添加修改不需要传入", hidden = true)
    private Long createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间，添加修改不需要传入", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除（1.删除，0正常）
     */
    @ApiModelProperty(value = "是否删除，添加修改不需要传入", hidden = true)
    @TableField("is_deleted")
    private Integer deleted;
}
