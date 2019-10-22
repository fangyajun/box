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
    @Transactional(rollbackFor = Exception.class)
    public Result creat(Integer userId, Integer prepayCardId) {
        // 1.判断预付金或服务卡是否正常
        BoxPrepayCard boxPrepayCard = boxPrepayCardService.getById(prepayCardId);
        if (boxPrepayCard == null || boxPrepayCard.getStatus() == 0) {
            return Result.failure("数据异常,查无此预付金服务卡或预付金服务卡未启用");
        }

        // 2.用户是否已有未关闭预付金或者服务卡订单
        QueryWrapper<BoxPrepayCardOrder> queryWrapper = new QueryWrapper<BoxPrepayCardOrder>().eq("deleted", 0).
                eq("user_id", userId);
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderMapper.selectOne(queryWrapper.in("order_status", 2, 3));
        if (prepayCardOrder != null) {
            return Result.failure("您有预付金或者服务卡未用完，不能再次申请！");
        }

        // 3.创建订单
        BoxPrepayCardOrder boxPrepayCardOrder = new BoxPrepayCardOrder();
        boxPrepayCardOrder.setUserId(userId);
        boxPrepayCardOrder.setPrepayCardId(prepayCardId);
        boxPrepayCardOrder.setOrderNo(CodeGeneratorUtil.getOrderCode());
        boxPrepayCardOrder.setCategory(boxPrepayCard.getCategory());
        boxPrepayCardOrder.setOrderStatus(0);
        boxPrepayCardOrder.setServiceTimes(boxPrepayCard.getServiceTimes());
        boxPrepayCardOrder.setPrepayPrice(boxPrepayCard.getRetailPrice());
        if (boxPrepayCard.getCategory() == 0) {
            // 是预付金，
            boxPrepayCardOrder.setVailableAmount(boxPrepayCard.getRetailPrice());
        } else {
            // 服务卡，者可用的预付金是0
            boxPrepayCardOrder.setVailableAmount(new BigDecimal(0));
        }
        boxPrepayCardOrder.setCouponPrice(new BigDecimal(0));
        boxPrepayCardOrder.setOrderPrice(boxPrepayCard.getRetailPrice().subtract(boxPrepayCardOrder.getCouponPrice()));
        boxPrepayCardOrder.setRefund(0);
        boxPrepayCardOrder.setRefundPrepayAmounts(new BigDecimal(0));
        boxPrepayCardOrder.setAddTime(System.currentTimeMillis());
        boxPrepayCardOrder.setUpdateTime(System.currentTimeMillis());
        boxPrepayCardOrderMapper.insert(boxPrepayCardOrder);

        return Result.success().setData("boxPrepayCardOrder", boxPrepayCardOrder);
    }

    @Override
    public int updateWithOptimisticLocker(BoxPrepayCardOrder boxPrepayCardOrder) {
        Long updateTime = boxPrepayCardOrder.getUpdateTime();
        boxPrepayCardOrder.setUpdateTime(System.currentTimeMillis());
        QueryWrapper<BoxPrepayCardOrder> queryWrapper = new QueryWrapper<BoxPrepayCardOrder>().eq("id", boxPrepayCardOrder.getId()).eq("update_time", updateTime);
        return boxPrepayCardOrderMapper.update(boxPrepayCardOrder, queryWrapper);
    }
}
