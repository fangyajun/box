package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.CodeGeneratorUtil;
import com.kuose.box.db.order.dao.BoxOrderMapper;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.db.user.entity.BoxUserAddress;
import com.kuose.box.db.user.entity.BoxUserBase;
import com.kuose.box.wx.login.service.BoxUserService;
import com.kuose.box.wx.order.service.BoxOrderService;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import com.kuose.box.wx.user.service.BoxUserAddressService;
import com.kuose.box.wx.user.service.BoxUserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private BoxUserAddressService boxUserAddressService;
    @Autowired
    private BoxUserService boxUserService;
    @Autowired
    private BoxUserBaseService boxUserBaseService;


    @Override
    public Result create(BoxOrder boxOrder) {
        BoxUserAddress userAddress = boxUserAddressService.getOne(new QueryWrapper<BoxUserAddress>().eq("deleted", 0).
                eq("user_id", 0).eq("is_default", 0));
        if (userAddress == null) {
            return Result.failure(507, "您没有设置默认收货地址，请先设置一个默认收货地址");
        }

        BoxUser boxUser = boxUserService.getById(boxOrder.getUserId());

        QueryWrapper<BoxPrepayCardOrder> queryWrapper = new QueryWrapper<BoxPrepayCardOrder>().eq("deleted", 0).
                eq("user_id", boxOrder.getUserId());
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getOne(queryWrapper.gt("vailable_amount", 0));

        BoxUserBase userBase = boxUserBaseService.getOne(new QueryWrapper<BoxUserBase>().eq("deleted", 0).eq("user_id", boxOrder.getUserId()));
        if (userBase == null) {
            return Result.failure(507, "请先设置您期望的收货时间");
        }

        boxOrder.setOrderNo(CodeGeneratorUtil.getOrderCode());
        boxOrder.setOrderStatus(0);
        boxOrder.setAuditStatus(0);
        boxOrder.setAddrId(userAddress.getId());
        boxOrder.setNicName(boxUser.getNickname());
        boxOrder.setMobile(userAddress.getPhone());
        // TODO 优惠券待后期做 boxOrder.setCouponPrice();

        if (prepayCardOrder == null) {
            // 不是预付金
            boxOrder.setAdvancePrice(new BigDecimal(0));
        } else {
            // 有预付金
            boxOrder.setAdvancePrice(prepayCardOrder.getVailableAmount());
        }
        boxOrder.setExpectTime(userBase.getExpectTime());

        boxOrder.setAddTime(System.currentTimeMillis());
        boxOrder.setUpdateTime(System.currentTimeMillis());

        return Result.success().setData("boxOrder", boxOrder);
    }
}
