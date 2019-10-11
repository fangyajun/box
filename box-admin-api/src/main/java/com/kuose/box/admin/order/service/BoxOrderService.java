package com.kuose.box.admin.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.admin.order.dto.OrderDto;
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

    IPage<BoxOrder> listOrderPage(Page<BoxOrder> boxOrderPage, OrderDto orderDto);

    int updateWithOptimisticLocker(BoxOrder order);
}
