package com.kuose.box.wx.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.db.order.entity.BoxOrderGoodsComment;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.order.service.BoxOrderGoodsCommentService;
import com.kuose.box.wx.order.service.BoxOrderGoodsService;
import com.kuose.box.wx.order.service.BoxOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单商品表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Api(tags = {"订单，订单商品"})
@RestController
@RequestMapping("/boxOrderGoods")
public class BoxOrderGoodsController {

    @Autowired
    private BoxOrderGoodsService boxOrderGoodsService;
    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private BoxOrderGoodsCommentService boxOrderGoodsCommentService;


    @ApiOperation(value="盒子商品列表")
    @GetMapping("/boxDetail")
    public Result boxDetail(Integer orderId, @ApiParam(hidden = true) @LoginUser Integer userId) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (orderId == null) {
            return Result.failure("缺少必传参数");
        }

        List<BoxOrderGoods> boxOrderGoodsList = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("deleted", 0));
        BoxOrder boxOrder = boxOrderService.getById(orderId);
        String coordinatorMessage = "";
        if (boxOrder != null) {
            coordinatorMessage = boxOrder.getCoordinatorMessage();
        }
        return Result.success().setData("boxOrderGoodsList", boxOrderGoodsList).setData("coordinatorMessage", coordinatorMessage);
    }

    @ApiOperation(value="盒子商品详情，包括评价信息")
    @GetMapping("/orderGoodsDetail")
    public Result orderGoodsDetail(Integer orderGoodsId) {
        if (orderGoodsId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrderGoods orderGoods = boxOrderGoodsService.getById(orderGoodsId);
        BoxOrderGoodsComment orderGoodsComment = boxOrderGoodsCommentService.getOne(new QueryWrapper<BoxOrderGoodsComment>().eq("order_id", orderGoodsId).
                eq("sku_id", orderGoods.getSkuId()).eq("deleted", 0));

        return Result.success().setData("orderGoods", orderGoods).setData("orderGoodsComment", orderGoodsComment);
    }

    @ApiOperation(value="盒子商品评价")
    @PostMapping("/goodsAppraisement")
    public Result goodsAppraisement(@RequestBody BoxOrderGoodsComment boxOrderGoodsComment, @ApiParam(hidden = true) @LoginUser Integer userId ) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (boxOrderGoodsComment.getOrderId() == null || boxOrderGoodsComment.getSkuId() == null) {
            return Result.failure("缺少必传参数");
        }
        if (boxOrderGoodsComment.getOrderGoodsStatus() == null) {
            return Result.failure(506, "必须对商品做出保留，退货，换货选择");
        }

        boxOrderGoodsService.goodsAppraisement(boxOrderGoodsComment);
        return Result.success();
    }

    @ApiOperation(value="需要退还的商品列表，预约取件列表调用")
    @GetMapping("/listBackGoods")
    public Result listBackGoods(Integer orderId, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (orderId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder boxOrder = boxOrderService.getById(orderId);
        if (boxOrder.getOrderStatus() != 5) {
            return Result.failure(506, "请先支付订单");
        }

        List<BoxOrderGoods> orderGoodsList = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("deleted", 0).
                eq("order_id", orderId).ne("order_goods_status", 1));

        return Result.success().setData("orderGoodsList", orderGoodsList);
    }


}

