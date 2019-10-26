package com.kuose.box.wx.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.order.dto.PrepayCardDTO;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 预付金或服务卡订单表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Api(tags = {"订单，预付金订单"})
@RestController
@RequestMapping("/boxPrepayCardOrder")
public class BoxPrepayCardOrderController {

    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;

    @ApiOperation(value="创建预付金或服务卡订单")
    @PostMapping("/creat")
    public Result creat(@RequestBody PrepayCardDTO prepayCardDTO, @ApiParam(hidden = true) @LoginUser Integer userId) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }

        if (prepayCardDTO.getPrepayCardId() == null) {
            return Result.failure("缺少必传参数");
        }

        // 先删除未付款的预付金或服务卡订单，在创建订单
        boxPrepayCardOrderService.remove(new QueryWrapper<BoxPrepayCardOrder>().eq("user_id",prepayCardDTO.getUserId()).in("order_status", 0,1));
        return boxPrepayCardOrderService.creat(prepayCardDTO.getUserId(), prepayCardDTO.getPrepayCardId());
    }

    @ApiOperation(value="获取用户的预付金订单")
    @GetMapping("/getPrepayCardOrder")
    public Result getPrepayCardOrder(Integer loginUserId, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        if (loginUserId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getOne(new QueryWrapper<BoxPrepayCardOrder>().eq("deleted", 0).eq("user_id", loginUserId).
                ne("order_status", 5));

        return Result.success().setData("prepayCardOrder", prepayCardOrder);
    }

    @ApiOperation(value="预付金订单注释")
    @PostMapping("/zhushi")
    public Result zhushi(@RequestBody BoxPrepayCardOrder boxPrepayCardOrder) {
        return Result.success();
    }

}

