package com.kuose.box.db.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 尺寸表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
@Data
public class BoxGoodsSize extends Model<BoxGoodsSize> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 尺寸名称
     */
    private String sizeName;

    /**
     * 尺寸编码
     */
    private String sizeCode;

    /**
     * 状态,0:正常状态
     */
    private Integer colorStatus;

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
