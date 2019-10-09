package com.kuose.box.db.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 问题视图类别表
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-10-09
 */
public class BoxSurveyQuestionViewType extends Model<BoxSurveyQuestionViewType> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * code
     */
    private String viewTypeCode;

    /**
     * 视图类型，可以用json表示
     */
    private String viewTypeName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getViewTypeCode() {
        return viewTypeCode;
    }

    public void setViewTypeCode(String viewTypeCode) {
        this.viewTypeCode = viewTypeCode;
    }

    public String getViewTypeName() {
        return viewTypeName;
    }

    public void setViewTypeName(String viewTypeName) {
        this.viewTypeName = viewTypeName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BoxSurveyQuestionViewType{" +
        "id=" + id +
        ", viewTypeCode=" + viewTypeCode +
        ", viewTypeName=" + viewTypeName +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
