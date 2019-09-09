package com.kuose.box.admin.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.order.dto.OrderGoodsDto;
import com.kuose.box.admin.order.entity.BoxOrder;
import com.kuose.box.admin.order.entity.BoxOrderGoods;
import com.kuose.box.admin.order.service.BoxOrderGoodsService;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"搭配管理，盒子商品管理"})
@RestController
@RequestMapping("/boxOrderGoods")
public class BoxOrderGoodsController {

    @Autowired
    private BoxOrderGoodsService boxOrderGoodsService;
    @Autowired
    private BoxOrderService boxOrderService;

    @ApiOperation(value="添加商品到盒子")
    @PostMapping("/addOrderGoods")
    public Result addOrderGoods(@RequestBody OrderGoodsDto orderGoodsDto) {
        if (orderGoodsDto.getOrderId() == null || orderGoodsDto.getSkuIds() == null) {
            return Result.failure("缺少必传参数");
        }
        BoxOrder boxOrder = boxOrderService.getById(orderGoodsDto.getOrderId());
        if (boxOrder == null) {
            return Result.failure("数据异常,该盒子订单不存在");
        }
        if (boxOrder.getAuditStatus() == 1) {
            return Result.failure("该盒子已搭配审核，无法操作！");
        }

        if (!StringUtil.isBlank(orderGoodsDto.getCoordinatorMessage())) {
            boxOrder.setCoordinatorMessage(orderGoodsDto.getCoordinatorMessage());
            boxOrderService.updateById(boxOrder);
        }

        boxOrderGoodsService.save(orderGoodsDto);
        return Result.success();
    }

    @ApiOperation(value="盒子详情")
    @GetMapping("/boxDetail")
    public Result boxDetail(Integer orderId) {
        if (orderId == null) {
            return Result.failure("缺少必传参数");
        }

        List<BoxOrderGoods> boxOrderGoodsList = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("deleted", 0));
        BoxOrder boxOrder = boxOrderService.getById(orderId);
        return Result.success().setData("boxOrderGoodsList", boxOrderGoodsList).setData("coordinatorMessage", boxOrder.getCoordinatorMessage());
    }

//    @ApiOperation(value="删除盒子商品，参数传订单id和skuId")
//    @PostMapping("/delete")
//    public Result delete(@RequestBody BoxOrderGoods boxOrderGoods) {
//        if (boxOrderGoods.getOrderId() == null || boxOrderGoods.getSkuId() == null) {
//            return Result.failure("缺少必传参数");
//        }
//
//        BoxOrder boxOrder = boxOrderService.getById(boxOrderGoods.getOrderId());
//        if (boxOrder == null) {
//            return Result.failure("数据异常,该盒子订单不存在");
//        }
//        if (boxOrder.getAuditStatus() == 1) {
//            return Result.failure("该盒子已搭配审核，无法操作！");
//        }
//
//        boolean b = boxOrderGoodsService.remove(new QueryWrapper<BoxOrderGoods>().eq("order_id", boxOrderGoods.getOrderId()).eq("sku_id", boxOrderGoods.getSkuId()));
//        if (!b) {
//            return Result.failure(500,"删除数据异常");
//        }
//        return Result.success();
//    }
}

