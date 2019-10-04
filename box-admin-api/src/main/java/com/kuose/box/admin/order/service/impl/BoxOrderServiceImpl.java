package com.kuose.box.admin.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.order.dto.OrderDto;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.db.order.dao.BoxOrderMapper;
import com.kuose.box.db.order.entity.BoxOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Service
public class BoxOrderServiceImpl extends ServiceImpl<BoxOrderMapper, BoxOrder> implements BoxOrderService {
    @Autowired
    private BoxOrderMapper boxOrderMapper;

    @Override
    public IPage<BoxOrder> listOrderPage(Page<BoxOrder> boxOrderPage, OrderDto orderDto) {
        return boxOrderMapper.listOrderPage(boxOrderPage,orderDto.getOrderNo(), orderDto.getMinExpectTime(),
                orderDto.getMaxExpectTime(),orderDto.getMobile(), orderDto.getOrderStatus(), orderDto.getAuditStatus());
    }
}
