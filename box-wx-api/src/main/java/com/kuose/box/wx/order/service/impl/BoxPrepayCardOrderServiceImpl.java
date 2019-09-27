package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.CodeGeneratorUtil;
import com.kuose.box.db.prepay.dao.BoxPrepayCardOrderMapper;
import com.kuose.box.db.prepay.entity.BoxPrepayCard;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import com.kuose.box.wx.order.service.BoxPrepayCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 预付金或服务卡订单表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Service
public class BoxPrepayCardOrderServiceImpl extends ServiceImpl<BoxPrepayCardOrderMapper, BoxPrepayCardOrder> implements BoxPrepayCardOrderService {

    @Autowired
    private BoxPrepayCardService boxPrepayCardService;
    @Autowired
    private BoxPrepayCardOrderMapper boxPrepayCardOrderMapper;

    @Override
    @Transactional
    public Result creat(BoxPrepayCardOrder boxPrepayCardOrder) {
        // 1.判断预付金或服务卡是否正常
        BoxPrepayCard boxPrepayCard = boxPrepayCardService.getById(boxPrepayCardOrder.getPrepayCardId());
        if (boxPrepayCard == null || boxPrepayCard.getStatus() == 0) {
            return Result.failure(503, "数据异常,查无此预付金服务卡或预付金服务卡未启用");
        }

        // 2.用户是否已有未关闭预付金或者服务卡订单
        QueryWrapper<BoxPrepayCardOrder> queryWrapper = new QueryWrapper<BoxPrepayCardOrder>().eq("deleted", 0).
                eq("user_id", boxPrepayCardOrder.getUserId());
        Integer cardCount = boxPrepayCardOrderMapper.selectCount(queryWrapper.ge("service_times", 0));
        Integer prepayCount = boxPrepayCardOrderMapper.selectCount(queryWrapper.gt("vailable_amount", 0));
        if (cardCount >= 1 || prepayCount >= 1) {
            return Result.failure(505, "您有预付金或者服务卡未用完，不能再次申请！");
        }

        // 3.创建订单
        boxPrepayCardOrder.setOrderNo(CodeGeneratorUtil.getOrderCode());
        boxPrepayCardOrder.setCategory(boxPrepayCard.getCategory());
        boxPrepayCardOrder.setOrderStatus(0);
        if (boxPrepayCard.getCategory() == 0) {
            // 预付金
            boxPrepayCardOrder.setServiceTimes(-2);
        }else {
            // 服务卡
            boxPrepayCardOrder.setServiceTimes(-1);
        }
        boxPrepayCardOrder.setVailableAmount(new BigDecimal(0));
        boxPrepayCardOrder.setPrepayPrice(boxPrepayCard.getRetailPrice());
        boxPrepayCardOrder.setCouponPrice(new BigDecimal(0));
        boxPrepayCardOrder.setOrderPrice(boxPrepayCard.getRetailPrice().subtract(boxPrepayCardOrder.getCouponPrice()));
        boxPrepayCardOrder.setAddTime(System.currentTimeMillis());
        boxPrepayCardOrder.setUpdateTime(System.currentTimeMillis());
        boxPrepayCardOrderMapper.insert(boxPrepayCardOrder);

        return Result.success().setData("boxPrepayCardOrder", boxPrepayCardOrder);
    }
}
