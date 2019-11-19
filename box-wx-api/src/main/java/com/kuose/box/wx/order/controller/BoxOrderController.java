package com.kuose.box.wx.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.order.entity.BoxOrderComment;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.express.entity.ExpressInfo;
import com.kuose.box.wx.express.service.ExpressService;
import com.kuose.box.wx.order.dto.CancelOrderTDO;
import com.kuose.box.wx.order.dto.OrderTDO;
import com.kuose.box.wx.order.service.BoxOrderGoodsService;
import com.kuose.box.wx.order.service.BoxOrderService;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Api(tags = {"订单，用户订单"})
@RestController
@RequestMapping("/boxOrder")
public class BoxOrderController {

    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;
    @Autowired
    private BoxOrderGoodsService boxOrderGoodsService;
    @Autowired
    private ExpressService expressService;

    @ApiOperation(value="创建用户订单，要盒子的时候可以调用,返回状态505表示 用户需要去支付预付金订单")
    @PostMapping("/create")
    public Result create(@RequestBody OrderTDO orderTDO, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        if (orderTDO.getAddrId() == null) {
            return Result.failure("请先选择或填写一个收货地址");
        }

        // 判断用户是否有订单在搭配中或者未完成
        QueryWrapper<BoxOrder> orderQueryWrapper = new QueryWrapper<BoxOrder>().eq("deleted", 0).eq("user_id", orderTDO.getUserId()).notIn("order_status", 8,9,10);
        if (boxOrderService.count(orderQueryWrapper) >= 1) {
            return Result.failure("您有一个订单在进行中，暂时无法要盒子！");
        }

        // 2.用户是否已有已付款预付金或者服务卡订单
        QueryWrapper<BoxPrepayCardOrder> queryWrapper = new QueryWrapper<BoxPrepayCardOrder>().eq("deleted", 0).
                eq("user_id", orderTDO.getUserId());
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getOne(queryWrapper.in("order_status", 2, 3));
        if (prepayCardOrder == null) {
            return Result.failure(505, "您没有支付预付金服务卡，请前往支付预付金");
        }

        return boxOrderService.create(orderTDO.getUserId(), orderTDO.getAddrId(), prepayCardOrder.getId());
    }


    @ApiOperation(value="获取进行中的订单")
    @GetMapping("/underwayOrder")
    public Result underwayOrder(Integer useId, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }
        Result result = Result.success();

        // 查询未关闭的订单
        BoxOrder order = boxOrderService.getOne(new QueryWrapper<BoxOrder>().eq("user_id", useId).eq("deleted", 0).
                notIn("order_status", 8,9,10));
        if (order != null) {
            // 订单是已发货状态，且物流信息不能为空，查询物流信息
            if (order.getOrderStatus() != 0 && order.getOrderStatus() != 1) {
                if (!StringUtil.isBlank(order.getShipChannel()) && !StringUtil.isBlank(order.getShipSn())) {
                    ExpressInfo expressInfo = expressService.getExpressInfo(order.getShipChannel(), order.getShipSn());
                    result.setData("expressInfo", expressInfo);
                }
            }
        }

        return result.setData("order", order);
    }


    @ApiOperation(value="用户订单列表")
    @GetMapping("/list")
    public Result list(Integer useId, Integer orderStatus, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")  Integer limit, @ApiParam(hidden = true)@LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }
        Page<BoxOrder> boxOrderPage = new Page<>();
        boxOrderPage.setSize(limit);
        boxOrderPage.setCurrent(page);

        QueryWrapper<BoxOrder> queryWrapper = new QueryWrapper<BoxOrder>().eq("user_id", useId).
                eq("deleted", 0);
        if (orderStatus != null) {
            queryWrapper.eq("order_status", orderStatus);
        }
        IPage<BoxOrder> orderIPage = boxOrderService.page(boxOrderPage, queryWrapper);

        return Result.success().setData("orderIPage", orderIPage);
    }

    @ApiOperation(value="用户订单详情")
    @GetMapping("/detail")
    public Result detail(Integer orderId, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        Result result = Result.success();

        if (orderId == null) {
            return Result.failure(506, "订单id不能为空");
        }


        BoxOrder boxOrder = boxOrderService.getById(orderId);
        if (boxOrder == null) {
            return Result.failure(506, "订单不存在");
        }

        if (!boxOrder.getUserId().equals(userId)) {
            return Result.failure(506, "不是当前用户的订单");
        }

        // 订单是已发货状态，且物流信息不能为空，查询物流信息
        if (boxOrder.getOrderStatus() != 0 && boxOrder.getOrderStatus() != 1) {
            if (!StringUtil.isBlank(boxOrder.getShipChannel()) && !StringUtil.isBlank(boxOrder.getShipSn())) {
                ExpressInfo expressInfo = expressService.getExpressInfo(boxOrder.getShipChannel(), boxOrder.getShipSn());
                result.setData("expressInfo", expressInfo);
            }
        }

        // 用户未确认收货，不能查看盒子商品详情
        if (boxOrder.getOrderStatus() != 0 && boxOrder.getOrderStatus() != 1 && boxOrder.getOrderStatus() != 2) {
            List<BoxOrderGoods> boxOrderGoodsList  = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("deleted", 0));
            result.setData("boxOrderGoodsList", boxOrderGoodsList);
        }

        return result.setData("boxOrder", boxOrder);
    }

    @ApiOperation(value="修改订单的收货地址")
    @PostMapping("/updateOrderAddr")
    public Result updateOrderAddr(@RequestBody OrderTDO orderTDO, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        if (orderTDO.getOrderId() == null || orderTDO.getAddrId() == null) {
            return Result.failure("参数订单id必传");
        }

        BoxOrder order = boxOrderService.getById(orderTDO.getOrderId());
        if (order == null) {
            return Result.failure(507, "数据异常，查无此订单");
        }

        if (order.getOrderStatus() != 0 && order.getOrderStatus() != 1) {
            return Result.failure(506, "订单已发货，无法更改收货地址");
        }

        order.setAddrId(orderTDO.getAddrId());

        if (boxOrderService.updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("数据更新异常");
        }
        return Result.success();
    }

    @ApiOperation(value="用户取消订单")
    @PostMapping("/cancel")
    public Result cancel(@RequestBody CancelOrderTDO cancelOrderTDO, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }
        return boxOrderService.cancel(cancelOrderTDO.getOrderId());
    }

    @ApiOperation(value="用户确认收货")
    @PostMapping("/confirm")
    public Result confirm(@RequestBody OrderTDO orderTDO, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }
        if (orderTDO.getOrderId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder order = boxOrderService.getById(orderTDO.getOrderId());
        if (order.getOrderStatus() != 2) {
            return Result.failure(506, "订单状态不是待收货状态，无法进行此操作！");
        }

        order.setOrderStatus(3);
        if (boxOrderService.updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("数据更新异常");
        }
        return Result.success();
    }

    @ApiOperation(value="订单评价")
    @PostMapping("/orderAppraisement")
    public Result orderAppraisement(@RequestBody BoxOrderComment boxOrderComment, @ApiParam(hidden = true) @LoginUser Integer userId ) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (boxOrderComment.getOrderId() == null ) {
            return Result.failure("缺少必传参数");
        }

        boxOrderService.orderAppraisement(boxOrderComment);
        return Result.success();
    }

    @ApiOperation(value="订单结算，订单结算页面")
    @GetMapping("/orderSettlement")
    public Result orderSettlement(Integer orderId) {
        if (orderId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder boxOrder = boxOrderService.getById(orderId);
        if (boxOrder.getOrderStatus() != 3 && boxOrder.getOrderStatus() != 4) {
            return Result.failure(506, "请先确认收货，在进行订单结算");
        }

        return boxOrderService.orderSettlement(orderId);
    }

    @ApiOperation(value="订单是否有商品需要寄回, 返回结果，0：否，1：是,有商品寄回")
    @GetMapping("/isToBackGoods")
    public Result isToBackGoods(Integer orderId, @ApiParam(hidden = true) @LoginUser Integer userId) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (orderId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder boxOrder = boxOrderService.getById(orderId);
        if (boxOrder.getOrderStatus() != 5) {
            return Result.failure("请先支付订单");
        }

        List<BoxOrderGoods> orderGoodsList = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("deleted", 0).
                eq("order_id", orderId).ne("order_goods_status", 1));

        if (orderGoodsList == null || orderGoodsList.isEmpty()) {
            return Result.success().setData("isToBackGoods", 0);
        }

        return Result.success().setData("isToBackGoods", 1);
    }

    @ApiOperation(value="订单没有商品寄回，更新订单得状态为已完成")
    @PostMapping("/updateOrderStatusToFinish")
    public Result updateOrderStatusToFinish(@RequestBody OrderTDO orderTDO, @ApiParam(hidden = true) @LoginUser Integer userId) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (orderTDO.getOrderId() == null) {
            return Result.failure("缺少必传参数");
        }
        BoxOrder boxOrder = boxOrderService.getById(orderTDO.getOrderId());
        if (boxOrder.getOrderStatus() != 5) {
            return Result.failure("请先支付订单");
        }
        List<BoxOrderGoods> orderGoodsList = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("deleted", 0).
                eq("order_id", orderTDO.getOrderId()).ne("order_goods_status", 1));

        if (orderGoodsList != null && !orderGoodsList.isEmpty()) {
            return Result.failure("订单有商品待寄回，改变订单状态");
        }

        // 预付金订单状态
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(boxOrder.getPrepayCardOrderId());
        prepayCardOrder.setRefund(1);
        boxPrepayCardOrderService.updateById(prepayCardOrder);

        boxOrder.setOrderStatus(9);
        int update = boxOrderService.updateWithOptimisticLocker(boxOrder);
        if (update == 0) {
            return Result.failure("更新失败");
        }

        return Result.success();
    }
}

