package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.CodeGeneratorUtil;
import com.kuose.box.db.discount.entity.BoxDiscount;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import com.kuose.box.db.order.dao.BoxOrderGoodsMapper;
import com.kuose.box.db.order.dao.BoxOrderMapper;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.order.entity.BoxOrderComment;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.db.user.entity.BoxUserAddress;
import com.kuose.box.db.user.entity.BoxUserBase;
import com.kuose.box.wx.goods.service.BoxGoodsSkuService;
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
    @Autowired
    private BoxOrderGoodsMapper boxOrderGoodsMapper;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result create(Integer userId, Integer addrId, Integer prepayCardOrderId) {
        BoxUserAddress userAddress = boxUserAddressService.getById(addrId);
        if (userAddress == null) {
            return Result.failure("数据异常,查无此收货地址");
        }

        BoxUser boxUser = boxUserService.getById(userId);
        if (boxUser == null) {
            return Result.failure( "数据异常,查无此用户信息");
        }

        BoxUserBase userBase = boxUserBaseService.getOne(new QueryWrapper<BoxUserBase>().eq("deleted", 0).eq("user_id", userId));
        if (userBase == null || userBase.getExpectTime() == null) {
            return Result.failure("请先设置您期望的收货时间");
        }

        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(prepayCardOrderId);

        BoxOrder boxOrder = new BoxOrder();
        boxOrder.setUserId(userId);
        boxOrder.setAddrId(addrId);
        boxOrder.setUserMessage(userBase.getLeaveWord());
        boxOrder.setOrderNo(CodeGeneratorUtil.getOrderCode());
        boxOrder.setOrderStatus(0);
        boxOrder.setAuditStatus(0);
        boxOrder.setNicName(boxUser.getNickname());
        boxOrder.setMobile(userAddress.getPhone());
        // TODO 优惠券待后期做 boxOrder.setCouponPrice();
        boxOrder.setPrepayCardOrderId(prepayCardOrderId);
        boxOrder.setAdvancePrice(prepayCardOrder.getVailableAmount());
        boxOrder.setRefundPrepayAmounts(new BigDecimal("0"));
        boxOrder.setExpectTime(userBase.getExpectTime());
        boxOrder.setAddTime(System.currentTimeMillis());
        boxOrder.setUpdateTime(System.currentTimeMillis());

        boxOrderMapper.insert(boxOrder);
        return Result.success().setData("boxOrder", boxOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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


        boxOrder.setGoodsPrice(goodsPrice);
        boxOrder.setDiscountPrice(discountPrice);
        // 设置订单状态为“4-已结算待支付”状态
        boxOrder.setOrderStatus(4);
        // 订单费用
        // 订单费用 = 商品总费用 - 优惠券减免 - 折扣减免
        if (goodsPrice.compareTo(new BigDecimal("0")) == 1 && goodsPrice.compareTo(boxOrder.getCouponPrice().add(discountPrice)) == 1) {
            boxOrder.setOrderPrice(goodsPrice.subtract(boxOrder.getCouponPrice()).subtract(discountPrice));
        } else {
            boxOrder.setOrderPrice(new BigDecimal("0"));
        }

        // 当订单费用 > 预付款
        // 实付费用 = 订单费用 - 预付款
        // 可退的预付金 = 0
        if (boxOrder.getOrderPrice().compareTo(boxOrder.getAdvancePrice()) == 1) {
            boxOrder.setActualPrice(boxOrder.getOrderPrice().subtract(boxOrder.getAdvancePrice()));
            boxOrder.setRefundPrepayAmounts(new BigDecimal("0"));
        }
        // 当订单费用 <= 预付款:
        // 实付费用 = 0
        // 可退的预付金 = 预付金 - 订单费用
        if (boxOrder.getOrderPrice().compareTo(boxOrder.getAdvancePrice()) == -1 || boxOrder.getOrderPrice().compareTo(boxOrder.getAdvancePrice()) == 0) {
            boxOrder.setActualPrice(new BigDecimal("0"));
            boxOrder.setRefundPrepayAmounts(boxOrder.getAdvancePrice().subtract(boxOrder.getOrderPrice()));
        }

        if (updateWithOptimisticLocker(boxOrder) == 0) {
            throw new RuntimeException("数据更新异常");
        }

        return Result.success().setData("boxOrderGoodsList", boxOrderGoodsList).setData("boxOrder",boxOrder);
    }

    @Override
    public int updateWithOptimisticLocker(BoxOrder order) {
        Long updateTime = order.getUpdateTime();
        order.setUpdateTime(System.currentTimeMillis());
        QueryWrapper<BoxOrder> queryWrapper = new QueryWrapper<BoxOrder>().eq("id", order.getId()).eq("update_time", updateTime);
        return boxOrderMapper.update(order, queryWrapper);
    }

    @Override
    public int deleteWithOptimisticLocker(BoxOrder order) {
        Long updateTime = order.getUpdateTime();
        QueryWrapper<BoxOrder> queryWrapper = new QueryWrapper<BoxOrder>().eq("id", order.getId()).eq("update_time", updateTime);
        return boxOrderMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancel(Integer orderId) {
        BoxOrder order = boxOrderMapper.selectById(orderId);
        if (order.getOrderStatus() != 0 && order.getOrderStatus() != 1) {
            return Result.failure(506, "订单无法取消，订单已发货");
        }

        // 如订单已搭配商品 ,修改对应对应商品的库存在添加
        List<BoxOrderGoods> goodsList = boxOrderGoodsMapper.selectList(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("deleted", 0));
        if (goodsList != null && !goodsList.isEmpty()) {
            for (BoxOrderGoods boxOrderGoods : goodsList) {
                // 更新对应sku的库存
                BoxGoodsSku goodsSku = boxGoodsSkuService.getById(boxOrderGoods.getSkuId());
                goodsSku.setNumber(goodsSku.getNumber() + 1);
                boxGoodsSkuService.updateById(goodsSku);

                // 删除订单商品
                boxOrderGoodsMapper.deleteById(boxOrderGoods.getId());
            }
        }

        // 取消订单设置订单状态为10
        order.setOrderStatus(10);
        if (updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("取消订单异常");
        }

        return Result.success();
    }
}
