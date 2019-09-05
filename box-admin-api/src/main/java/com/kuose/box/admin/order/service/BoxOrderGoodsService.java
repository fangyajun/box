package com.kuose.box.admin.order.service;

import com.kuose.box.admin.order.dto.OrderGoodsDto;
import com.kuose.box.admin.order.entity.BoxOrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
public interface BoxOrderGoodsService extends IService<BoxOrderGoods> {

    void save(OrderGoodsDto orderGoodsDto);
}
