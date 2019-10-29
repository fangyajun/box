package com.kuose.box.admin.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.express.entity.ExpressInfo;
import com.kuose.box.admin.express.service.ExpressService;
import com.kuose.box.admin.order.dto.OrderDto;
import com.kuose.box.admin.order.dto.ShipDTO;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.admin.prepay.service.BoxPrepayCardOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

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
    @Autowired
    private ExpressService expressService;
    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;
    @Autowired
    private RestTemplate restTemplate;

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
    @PostMapping("/zhushi")
    public Result zhshi(@RequestBody BoxOrder boxOrder) {
        return Result.success();
    }

    @ApiOperation(value="获取物流信息详情")
    @GetMapping("/getExpressDetail")
    public Result getExpressDetail(String expCode, String expNo) {
        ExpressInfo expressInfo = expressService.getExpressDetail(expCode, expNo);
        return Result.success().setData("expressInfo", expressInfo);
    }

    @ApiOperation(value="确认收到用户寄回的商品")
    @GetMapping("/confirmUserBackGoods")
    public Result confirmUserBackGoods(Integer orderId) {
        if (orderId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder boxOrder = boxOrderService.getById(orderId);
        if (boxOrder == null) {
            return Result.failure("数据异常，查无此订单信息");
        }

        if (boxOrder.getOrderStatus() != 6 && boxOrder.getOrderStatus() != 7) {
            return Result.failure("订单状态异常，订单状态不是寄回状态");
        }

        boxOrderService.confirmUserBackGoods(orderId);

        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(boxOrder.getPrepayCardOrderId());
        if (prepayCardOrder.getRefund() == 1 && prepayCardOrder.getRefundPrepayAmounts().compareTo(new BigDecimal(0)) == 1) {
            // 退回预付金
            String refundPrePayMoneyUrl = "https://kuose.mynatapp.cc/payController/refundPrePayMoney?prepayCardOrderId=" + boxOrder.getPrepayCardOrderId();
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);
            ResponseEntity<JSONObject> exchange = restTemplate.exchange(refundPrePayMoneyUrl, HttpMethod.GET, httpEntity, JSONObject.class);
            JSONObject jsonResult = exchange.getBody();
            int status = jsonResult.getIntValue("status");
            if (status != 0) {
                return Result.failure(jsonResult.getString("info"));
            }
            return Result.success();
        }

         // 修改订单状态为已完成
        BoxOrder order = boxOrderService.getById(orderId);
        order.setOrderStatus(9);
        boxOrderService.updateById(order);

        return Result.success();
    }

}

