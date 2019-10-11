package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.CodeGeneratorUtil;
import com.kuose.box.db.discount.entity.BoxDiscount;
import com.kuose.box.db.order.dao.BoxOrderMapper;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.order.entity.BoxOrderComment;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.db.user.entity.BoxUserAddress;
import com.kuose.box.db.user.entity.BoxUserBase;
import com.kuose.box.wx.login.service.BoxUserService;
import com.kuose.box.wx.order.service.*;
import com.kuose.box.wx.user.service.BoxUserAddressService;
import com.kuose.box.wx.user.service.BoxUserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    private BoxPrepayCardOrderService boxPrepayCardOrderService;
    @Autowired
    private BoxUserAddressService boxUserAddressService;
    @Autowired
    private BoxUserService boxUserService;
    @Autowired
    private BoxUserBaseService boxUserBaseService;
    @Autowired
    private BoxOrderCommentService boxOrderCommentService;
    @Autowired
    private BoxOrderGoodsService boxOrderGoodsService;
    @Autowired
    private BoxDiscountService boxDiscountService;
    @Autowired
    private BoxOrderMapper boxOrderMapper;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void orderAppraisement(BoxOrderComment boxOrderComment) {
        BoxOrderComment getOne = boxOrderCommentService.getOne(new QueryWrapper<BoxOrderComment>().eq("order_id", boxOrderComment.getOrderId()));
        if (getOne == null) {
            boxOrderComment.setAddTime(System.currentTimeMillis());
            boxOrderComment.setUpdateTime(System.currentTimeMillis());

            boxOrderCommentService.save(boxOrderComment);
        } else {
            getOne.setCustomize(boxOrderComment.getCustomize());
            getOne.setServe(boxOrderComment.getServe());
            getOne.setStyle(boxOrderComment.getStyle());
            getOne.setNextBox(boxOrderComment.getNextBox());
            getOne.setCoordinator(boxOrderComment.getCoordinator());
            getOne.setContent(boxOrderComment.getContent());
            getOne.setUpdateTime(System.currentTimeMillis());

            boxOrderCommentService.updateById(getOne);
        }
    }

    @Override
    @Transactional
    public Result orderSettlement(Integer orderId) {

        BoxOrder boxOrder = boxOrderMapper.selectById(orderId);

        QueryWrapper<BoxOrderGoods> orderGoodsQueryWrapper = new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("deleted", 0);

        // 留下商品总费用计算
        BigDecimal goodsPrice = new BigDecimal(0);
        List<BoxOrderGoods> boxOrderGoodsList = boxOrderGoodsService.list(orderGoodsQueryWrapper);
        for (BoxOrderGoods boxOrderGoods : boxOrderGoodsList) {
            if (boxOrderGoods.getOrderGoodsStatus() == 1) {
                // 只有保留的商品才计算进订单金额
                goodsPrice = goodsPrice.add(boxOrderGoods.getPrice());
            }
        }

        // 折扣计算
        BigDecimal discountPrice = new BigDecimal(0);
        int count = boxOrderGoodsService.count(orderGoodsQueryWrapper.eq("order_goods_status", 1));
        if (count != 0) {
            BoxDiscount boxDiscount = boxDiscountService.getOne(new QueryWrapper<BoxDiscount>().eq("goods_amount", count));
            discountPrice = goodsPrice.subtract(goodsPrice.multiply(new BigDecimal(boxDiscount.getDiscount())));
        }

        // 订单费用，实付费用
        boxOrder.setGoodsPrice(goodsPrice);
        boxOrder.setDiscountPrice(discountPrice);
        // 订单费用 = 商品总费用 - 优惠券减免 - 折扣减免
        if (goodsPrice.compareTo(new BigDecimal("0")) == 1 && goodsPrice.compareTo(boxOrder.getCouponPrice().add(discountPrice)) == 1) {
            boxOrder.setOrderPrice(goodsPrice.subtract(boxOrder.getCouponPrice()).subtract(discountPrice));
        } else {
            boxOrder.setOrderPrice(new BigDecimal("0"));
        }
        // 当订单费用 > 预付款: 实付费用 = 订单费用 - 预付款
        if (boxOrder.getOrderPrice().compareTo(boxOrder.getAdvancePrice()) == 1) {
            boxOrder.setActualPrice(boxOrder.getOrderPrice().subtract(boxOrder.getAdvancePrice()));
        }
        // 当订单费用 <= 预付款: 实付费用 = 0
        if (boxOrder.getOrderPrice().compareTo(boxOrder.getAdvancePrice()) == -1 || boxOrder.getOrderPrice().compareTo(boxOrder.getAdvancePrice()) == 0) {
            boxOrder.setOrderPrice(new BigDecimal("0"));
        }
        boxOrder.setUpdateTime(System.currentTimeMillis());

        boxOrderMapper.updateById(boxOrder);

        return Result.success().setData("boxOrderGoodsList", boxOrderGoodsList).setData("boxOrder",boxOrder);
    }
}
