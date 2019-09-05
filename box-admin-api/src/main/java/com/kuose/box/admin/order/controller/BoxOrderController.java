package com.kuose.box.admin.order.controller;


import com.kuose.box.admin.order.service.BoxOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Api(tags = {"搭配管理，订单管理"})
@RestController
@RequestMapping("/boxOrder")
public class BoxOrderController {

    @Autowired
    private BoxOrderService boxOrderService;

}

