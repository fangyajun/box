package com.kuose.box.admin.generalize.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 优惠券用户使用表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-02
 */
@Data
public class BoxCouponUser extends Model<BoxCouponUser> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 优惠券ID
     */
    private Integer couponId;

    /**
     * 使用状态, 如果是0则未使用；如果是1则已使用；如果是2则已过期；如果是3则已经下架；
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 使用时间
     */
    private Long usedTime;

    /**
     * 有效期开始时间
     */
    private Long startTime;

    /**
     * 有效期截至时间
     */
    private Long endTime;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 逻辑删除,0:正常，1：删除
     */
    @TableField("`is_deleted`")
    private Integer deleted;

}
