package com.kuose.box.admin.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.admin.order.dto.OrderGoodsDto;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.order.entity.BoxOrderGoods;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
public interface BoxOrderGoodsService extends IService<BoxOrderGoods> {

    Result save(OrderGoodsDto orderGoodsDto);
}
