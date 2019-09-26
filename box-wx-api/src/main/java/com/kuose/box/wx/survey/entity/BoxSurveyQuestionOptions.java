package com.kuose.box.wx.survey.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 问题选项表
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-24
 */
@Data
public class BoxSurveyQuestionOptions extends Model<BoxSurveyQuestionOptions> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id，添加不需要传入,编辑删除需要传入")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题的id
     */
    @ApiModelProperty(value = "问题的id", example = "1")
    private Integer questionId;

    /**
     * 选项的内容
     */
    @ApiModelProperty(value = "选项的内容", example = "贴身裁剪")
    private String optionContent;

    /**
     * 选项的图片
     */
    @ApiModelProperty(value = "选项的图片", example = "/sfdsdfs/sdfsdfs/sfdsdfs.jpg")
    private String optionPic;

    /**
     * 选项的标签编号，有选项的填空题需要填写
     */
    @ApiModelProperty(value = "选项的标签编号，有选项的填空题需要填写", example = "SG")
    private String labelCode;


    /**
     * 选项排序,数越大排序靠前
     */
    @ApiModelProperty(value = "选项排序,数越大排序靠前", example = "12")
    private Integer sort;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "充分展示身材")
    private String remark;

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
    @ApiModelProperty(value = "逻辑删除（1.删除，0正常", hidden = true)
    @TableField("is_deleted")
    private Integer deleted;

}
