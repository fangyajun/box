package com.kuose.box.db.survery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 问卷用户答案josn
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-21
 */
public class BoxUserAnswerJson extends Model<BoxUserAnswerJson> {

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
     * 用户答案json
     */
    private String answerJson;

    /**
     * 填写时间
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
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
        return "BoxUserAnswerJson{" +
        "id=" + id +
        ", userId=" + userId +
        ", answerJson=" + answerJson +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
