package com.kuose.box.admin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Data
public class BoxGoodsCategory extends Model<BoxGoodsCategory> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父分类，0,表示头分类
     */
    private Integer parentId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 商品分类编码
     */
    private String categoryCode;

    /**
     * 分类名称拼音
     */
    private String pinyin;

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
    private Integer deleted;

}
