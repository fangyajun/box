package com.kuose.box.admin.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 问卷问题标签
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-02
 */
@Data
public class BoxSurveyQusetionLabel extends Model<BoxSurveyQusetionLabel> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", example = "上装裁剪")
    private String labelName;

    /**
     * 标签编号
     */
    @ApiModelProperty(value = "标签编号", example = "SZCJ")
    private String labelCode;

    /**
     * 标签类型
     */
    @ApiModelProperty(value = "标签类型", hidden = true)
    private Integer labelType;

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

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value = "逻辑删除", hidden = true)
    private Integer deleted;

}
