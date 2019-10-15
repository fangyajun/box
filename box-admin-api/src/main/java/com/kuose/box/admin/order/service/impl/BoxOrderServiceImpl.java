package com.kuose.box.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.order.dto.OrderDto;
import com.kuose.box.admin.order.dto.ShipDTO;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.admin.prepay.service.BoxPrepayCardOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.order.dao.BoxOrderMapper;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;

    @Override
    public IPage<BoxOrder> listOrderPage(Page<BoxOrder> boxOrderPage, OrderDto orderDto) {
        return boxOrderMapper.listOrderPage(boxOrderPage,orderDto.getOrderNo(), orderDto.getMinExpectTime(),
                orderDto.getMaxExpectTime(),orderDto.getMobile(), orderDto.getOrderStatus(), orderDto.getAuditStatus());
    }

    @Override
    public int updateWithOptimisticLocker(BoxOrder order) {
        Long updateTime = order.getUpdateTime();
        order.setUpdateTime(System.currentTimeMillis());
        QueryWrapper<BoxOrder> queryWrapper = new QueryWrapper<BoxOrder>().eq("id", order.getId()).eq("update_time", updateTime);
        return boxOrderMapper.update(order, queryWrapper);
    }

    @Override
    @Transactional
    public Result ship(ShipDTO shipDTO) {
        BoxOrder order = boxOrderMapper.selectById(shipDTO.getOrderId());

        // 订单已发货就设置预付金服务中状态,可退预付金为0
        BoxPrepayCardOrder boxPrepayCardOrder = new BoxPrepayCardOrder();
        boxPrepayCardOrder.setId(order.getPrepayCardOrderId());
        // 预付金为不可退状态
        boxPrepayCardOrder.setRefund(0);
        boxPrepayCardOrder.setRefundPrepayAmounts(new BigDecimal(0));
        // 设置预付金订单状态为服务中
        boxPrepayCardOrder.setOrderStatus(3);
        boxPrepayCardOrderService.updateById(boxPrepayCardOrder);

        // 修改订单状态
        order.setOrderStatus(2);
        order.setShipSn(shipDTO.getShipSn());
        order.setShipChannel(shipDTO.getShipChannel());
        order.setShipTime(shipDTO.getShipTime());

        int update = updateWithOptimisticLocker(order);
        if (update == 0) {
            throw new RuntimeException("数据更新已失效");
        }

        return Result.success();
    }
}
