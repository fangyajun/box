package com.kuose.box.db.recommend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 推荐评价表
 * </p>
 *
 *
 * @author 魔舞清华
 * @since 2019-11-12
 */
@Data
public class BoxRecommendComment extends Model<BoxRecommendComment> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表的用户ID
     */
    private Integer userId;

    /**
     * 推荐表的id
     */
    private Integer boxRecommendId;

    /**
     * 问题1
     */
    private String key1;

    /**
     * 答案1
     */
    private String value1;

    /**
     * 问题2
     */
    private String key2;

    /**
     * 答案2
     */
    private String value2;

    /**
     * 问题3
     */
    private String key3;

    /**
     * 答案3
     */
    private String value3;

    /**
     * 问题4
     */
    private String key4;

    /**
     * 答案4
     */
    private String value4;

    /**
     * 问题5
     */
    private String key5;

    /**
     * 答案5
     */
    private String value5;

    /**
     * 问题6
     */
    private String key6;

    /**
     * 答案6
     */
    private String value6;

    /**
     * 问题7
     */
    private String key7;

    /**
     * 答案7
     */
    private String value7;

    /**
     * 问题8
     */
    private String key8;

    /**
     * 答案8
     */
    private String value8;

    /**
     * 创建时间 
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;


}
