package com.kuose.box.admin.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.express.entity.ExpressInfo;
import com.kuose.box.admin.express.service.ExpressService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.admin.order.dto.OrderAuditDto;
import com.kuose.box.admin.order.dto.OrderGoodsDto;
import com.kuose.box.admin.order.service.BoxOrderGoodsService;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
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
@Api(tags = {"订单管理，盒子商品管理"})
@RestController
@RequestMapping("/boxOrderGoods")
public class BoxOrderGoodsController {

    @Autowired
    private BoxOrderGoodsService boxOrderGoodsService;
    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;
    @Autowired
    private ExpressService expressService;

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

        Integer[] skuIds = orderGoodsDto.getSkuIds();
        for (Integer skuId : skuIds) {
            BoxGoodsSku goodsSku = boxGoodsSkuService.getById(skuId);
            if (goodsSku == null) {
                return Result.failure("数据异常，skuId:"+ skuId +"不存在，查无此sku信息");
            }
        }

        if (boxOrder.getAuditStatus() == 1) {
            return Result.failure("该盒子已搭配审核，无法操作！");
        }

        if (!StringUtil.isBlank(orderGoodsDto.getCoordinatorMessage())) {
            boxOrder.setCoordinatorMessage(orderGoodsDto.getCoordinatorMessage());
            boxOrder.setCoordinator(orderGoodsDto.getUsername());
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
        Result result = Result.success();

        List<BoxOrderGoods> boxOrderGoodsList = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("deleted", 0));
        BoxOrder boxOrder = boxOrderService.getById(orderId);
        if (boxOrder.getOrderStatus() != 0 && boxOrder.getOrderStatus() != 1) {
            if (!StringUtil.isBlank(boxOrder.getShipChannel()) && !StringUtil.isBlank(boxOrder.getShipSn())) {
                ExpressInfo expressInfo = expressService.getExpressDetail(boxOrder.getShipChannel(), boxOrder.getShipSn());
                result.setData("expressInfo", expressInfo);
            }
        }
        String coordinatorMessage = "";
        if (boxOrder != null) {
            coordinatorMessage = boxOrder.getCoordinatorMessage();
        }
        return result.setData("boxOrder", boxOrder).setData("boxOrderGoodsList", boxOrderGoodsList).setData("coordinatorMessage", coordinatorMessage);
    }

    @ApiOperation(value="搭配审核")
    @PostMapping("/audit")
    public Result audit(@RequestBody OrderAuditDto orderAuditDto) {
        if (orderAuditDto.getOrderId() == null || StringUtil.isBlank(orderAuditDto.getUsername())) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder boxOrder = boxOrderService.getById(orderAuditDto.getOrderId());
        int count = boxOrderGoodsService.count(new QueryWrapper<BoxOrderGoods>().eq("order_id", boxOrder.getId()).eq("deleted", 0));
        if (count < 1) {
            return Result.failure("该订单还未搭配任何商品");
        }

        if (boxOrder.getAuditStatus() == 1) {
            return Result.failure("该订单已审核通过");
        }

        // 只有订单通过审核通过才把订单状态改为已搭配状态
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

    @ApiOperation(value="取消审核")
    @PostMapping("/cancelAudit")
    public Result cancelAudit(@RequestBody OrderAuditDto orderAuditDto) {
        if (orderAuditDto.getOrderId() == null || StringUtil.isBlank(orderAuditDto.getUsername())) {
            return Result.failure("缺少必传参数");
        }

        if (orderAuditDto.getStatus() != 0) {
            return Result.failure("取消审核的状态必须为0");
        }

        BoxOrder boxOrder = boxOrderService.getById(orderAuditDto.getOrderId());

        if (boxOrder.getOrderStatus() != 0 && boxOrder.getOrderStatus() != 1) {
            return Result.failure("该订单已发货，无法取消审核");
        }

        if (boxOrder.getAuditStatus() == 0) {
            return Result.failure("该订单没有审核，无法取消审核");
        }

        // 只有订单通过审核通过才把订单状态改为已搭配状态
        boxOrder.setOrderStatus(0);
        boxOrder.setAuditStatus(0);
        boxOrder.setAuditor(orderAuditDto.getUsername());

        int update = boxOrderService.updateWithOptimisticLocker(boxOrder);
        if (update == 0) {
            throw new RuntimeException("数据更新已失效");
        }

        return Result.success();
    }
}

