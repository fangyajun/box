package com.kuose.box.admin.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.order.dto.OrderAuditDto;
import com.kuose.box.admin.order.dto.OrderGoodsDto;
import com.kuose.box.admin.order.service.BoxOrderGoodsService;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.order.entity.BoxOrderGoods;
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

    @ApiOperation(value="添加或修改商品到盒子")
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
            boxOrder.setUpdateTime(System.currentTimeMillis());
            boxOrderService.updateById(boxOrder);
        }

        return boxOrderGoodsService.save(orderGoodsDto);
    }


    @ApiOperation(value="盒子详情")
    @GetMapping("/boxDetail")
    public Result boxDetail(Integer orderId) {
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

    @ApiOperation(value="搭配审核")
    @PostMapping("/audit")
    public Result audit(@RequestBody OrderAuditDto orderAuditDto) {
        if (orderAuditDto.getOrderId() == null || StringUtil.isBlank(orderAuditDto.getUsername())) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder boxOrder = boxOrderService.getById(orderAuditDto.getOrderId());
        if (boxOrder.getAuditStatus() == 1) {
            return Result.failure("该订单已审核通过");
        }

        // 审核状态，1：审核通过，2：审核未通过 ,审核通过才把订单状态改为已搭配状态
        if (orderAuditDto.getStatus() == 1) {
            boxOrder.setOrderStatus(1);
        }
        boxOrder.setAuditStatus(orderAuditDto.getStatus());
        boxOrder.setAuditor(orderAuditDto.getUsername());

        int update = boxOrderService.updateWithOptimisticLocker(boxOrder);
        if (update == 0) {
            throw new RuntimeException("数据更新已失效");
        }

        return Result.success();
    }


}

