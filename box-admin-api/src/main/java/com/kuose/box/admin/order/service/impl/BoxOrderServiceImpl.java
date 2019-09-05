package com.kuose.box.admin.order.service.impl;

import com.kuose.box.admin.order.entity.BoxOrder;
import com.kuose.box.admin.order.dao.BoxOrderMapper;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
