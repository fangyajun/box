package com.kuose.box.wx.order.controller;


import com.kuose.box.common.config.Result;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 预付金或服务卡订单表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Api(tags = {"预付金订单"})
@RestController
@RequestMapping("/boxPrepayCardOrder")
public class BoxPrepayCardOrderController {

    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;

    @ApiOperation(value="创建预付金或服务卡订单")
    @PostMapping("/creat")
    public Result creat(@RequestBody BoxPrepayCardOrder boxPrepayCardOrder, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        if (boxPrepayCardOrder.getPrepayCardId() == null) {
            return Result.failure(503, "缺少必传参数");
        }

        return boxPrepayCardOrderService.creat(boxPrepayCardOrder);
    }

}
