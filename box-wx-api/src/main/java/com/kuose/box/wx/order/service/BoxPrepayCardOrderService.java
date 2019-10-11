package com.kuose.box.wx.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;

/**
 * <p>
 * 预付金或服务卡订单表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
public interface BoxPrepayCardOrderService extends IService<BoxPrepayCardOrder> {

    Result creat(Integer userId, Integer prepayCardId);
}
