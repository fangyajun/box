package com.kuose.box.db.discount.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 盒子折扣率
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-10-07
 */
@Data
public class BoxDiscount extends Model<BoxDiscount> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 留下商品数量
     */
    private Integer goodsAmount;

    /**
     * 折扣率，用字符串表示，如：85折—>用0.85表示
     */
    private String discount;

    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;

}
