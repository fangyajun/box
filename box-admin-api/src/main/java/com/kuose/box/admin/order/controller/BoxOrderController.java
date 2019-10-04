package com.kuose.box.admin.order.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.order.dto.OrderDto;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.order.entity.BoxOrder;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    public Result list(OrderDto orderDto, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10")  Integer limit) {
        Page<BoxOrder> boxOrderPage = new Page<>();
        boxOrderPage.setSize(limit);
        boxOrderPage.setCurrent(page);

        IPage<BoxOrder> boxOrderIPage = boxOrderService.listOrderPage(boxOrderPage, orderDto);
        return Result.success().setData("boxOrderIPage", boxOrderIPage);
    }

}

