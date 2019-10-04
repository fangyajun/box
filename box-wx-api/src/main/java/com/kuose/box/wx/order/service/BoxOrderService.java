package com.kuose.box.wx.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.order.entity.BoxOrder;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
public interface BoxOrderService extends IService<BoxOrder> {

    Result create(BoxOrder boxOrder);
}
