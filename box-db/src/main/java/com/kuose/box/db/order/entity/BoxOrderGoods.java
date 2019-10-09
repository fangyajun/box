package com.kuose.box.db.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 订单商品表
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Data
public class BoxOrderGoods extends Model<BoxOrderGoods> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单表的订单ID
     */
    private Integer orderId;

    /**
     * 商品表的商品ID
     */
    private Integer goodsId;

    /**
     * 商品表的商品ID
     */
    private Integer skuId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品编号
     */
    private String goodsNo;

    /**
     * sku编号
     */
    private String skuNo;

    /**
     * 商品sku的售价
     */
    private BigDecimal price;

    /**
     * 颜色名称
     */
    private String colorName;

    /**
     * 尺寸名称
     */
    private String sizeName;

    /**
     * 商品货品图片或者商品图片
     */
    private String picUrl;

    /**
     * 订单商品评论，如果是-1，则超期不能评价；如果是0，则可以评价；如果其他值，则是comment表里面的评论ID。
     */
    private Integer comment;

    /**
     * 盒子商品状态，0:默认状态，1：保留，2：退货，3：换货
     */
    private Integer orderGoodsStatus;

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
