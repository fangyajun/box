package com.kuose.box.admin.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.order.dto.OrderDto;
import com.kuose.box.admin.order.dto.ShipDTO;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"订单管理，订单管理"})
@RestController
@RequestMapping("/boxOrder")
public class BoxOrderController {

    @Autowired
    private BoxOrderService boxOrderService;

    @ApiOperation(value="订单列表")
    @GetMapping("/list")
    public Result list(OrderDto orderDto, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")  Integer limit) {
        Page<BoxOrder> boxOrderPage = new Page<>();
        boxOrderPage.setSize(limit);
        boxOrderPage.setCurrent(page);

        IPage<BoxOrder> boxOrderIPage = boxOrderService.listOrderPage(boxOrderPage, orderDto);
        return Result.success().setData("boxOrderIPage", boxOrderIPage);
    }

    @ApiOperation(value="订单发货")
    @PostMapping("/ship")
    public Result ship(@RequestBody ShipDTO shipDTO) {
        if (shipDTO.getOrderId() == null || StringUtil.isBlank(shipDTO.getShipSn()) || StringUtil.isBlank(shipDTO.getShipChannel())) {
            return Result.failure("缺少必传参数");
        }



        BoxOrder order = boxOrderService.getById(shipDTO.getOrderId());
        if (order == null) {
            return Result.failure(506, "数据异常，查无此订单");
        }

        if (order.getOrderStatus() != 1) {
            return Result.failure("该订单状态不是待发货状态，请检查订单状态！");
        }

        return boxOrderService.ship(shipDTO);


    }

    @ApiOperation(value="订单表注释")
    @PostMapping("/zhshi")
    public Result zhshi(@RequestBody BoxOrder boxOrder) {

        return Result.success();
    }

}

