package com.kuose.box.wx.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.wx.order.dto.OrderGoodsAppraisementDTO;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
public interface BoxOrderGoodsService extends IService<BoxOrderGoods> {

    void goodsAppraisement(OrderGoodsAppraisementDTO orderGoodsAppraisementDTO);
}
