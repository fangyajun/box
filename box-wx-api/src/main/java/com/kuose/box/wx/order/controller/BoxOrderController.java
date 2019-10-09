package com.kuose.box.wx.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.order.entity.BoxOrderComment;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.order.service.BoxOrderService;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Api(tags = {"用户订单管理"})
@RestController
@RequestMapping("/boxOrder")
public class BoxOrderController {

    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;


    @ApiOperation(value="创建用户订单，要盒子的时候可以调用")
    @PostMapping("/create")
    public Result create(@RequestBody BoxOrder boxOrder, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        // 判断用户是否有订单在搭配中或者未完成
        QueryWrapper<BoxOrder> orderQueryWrapper = new QueryWrapper<BoxOrder>().eq("deleted", 0).eq("user_id", userId).ne("order_status", 7);
        if (boxOrderService.count(orderQueryWrapper) >= 1) {
            return Result.failure(506, "您有一个订单未完成，暂时无法要盒子！");
        }

        // 判断用户是否有预付金
        // 2.用户是否已有未关闭预付金或者服务卡订单
        QueryWrapper<BoxPrepayCardOrder> queryWrapper = new QueryWrapper<BoxPrepayCardOrder>().eq("deleted", 0).
                eq("user_id", userId);
        Integer cardCount = boxPrepayCardOrderService.count(queryWrapper.ge("service_times", 0));
        Integer prepayCount = boxPrepayCardOrderService.count(queryWrapper.gt("vailable_amount", 0));
        if (cardCount < 1 && prepayCount < 1) {
            return Result.failure(505, "您没有支付预付金服务卡，请前往支付预付金");
        }

        boxOrder.setUserId(userId);
        return boxOrderService.create(boxOrder);
    }


    @ApiOperation(value="修改订单的收货地址")
    @PostMapping("/update")
    public Result update(@RequestBody BoxOrder boxOrder, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        if (boxOrder.getId() == null) {
            return Result.failure("参数订单id必传");
        }

        BoxOrder order = boxOrderService.getById(boxOrder.getId());
        order.setAddrId(boxOrder.getAddrId());
        order.setUpdateTime(System.currentTimeMillis());

        boxOrderService.updateById(boxOrder);
        return Result.success();
    }

    @ApiOperation(value="用户取消订单")
    @PostMapping("/cancel")
    public Result cancel(@RequestBody BoxOrder boxOrder, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        BoxOrder order = boxOrderService.getById(boxOrder.getId());
        if (order.getOrderStatus() != 0 && order.getOrderStatus() != 1) {
            return Result.failure(507, "订单无法取消，订单已发货");
        }

        boxOrderService.removeById(boxOrder.getId());
        return Result.success();
    }

    @ApiOperation(value="用户确认收货")
    @PostMapping("/confirm")
    public Result confirm(@RequestBody BoxOrder boxOrder, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        if (boxOrder.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder order = boxOrderService.getById(boxOrder.getId());
        if (order.getOrderStatus() != 2) {
            return Result.failure(506, "订单状态不是待收货状态，无法进行此操作！");
        }

        order.setOrderStatus(3);
        boxOrderService.updateById(order);
        return Result.success();
    }

    @ApiOperation(value="订单评价")
    @PostMapping("/orderAppraisement")
    public Result orderAppraisement(@RequestBody BoxOrderComment boxOrderComment, @ApiParam(hidden = true) @LoginUser Integer userId ) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }
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
        if (boxOrder.getOrderStatus() != 3) {
            return Result.failure(506, "请先确认收货，在进行订单结算");
        }

        return boxOrderService.orderSettlement(orderId);



    }



}

