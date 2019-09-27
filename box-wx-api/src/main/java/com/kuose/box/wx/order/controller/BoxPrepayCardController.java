package com.kuose.box.wx.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.prepay.entity.BoxPrepayCard;
import com.kuose.box.wx.order.service.BoxPrepayCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 预付金或服务卡表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Api(tags = {"预付金或服务卡展示"})
@RestController
@RequestMapping("/boxPrepayCard")
public class BoxPrepayCardController {

    @Autowired
    private BoxPrepayCardService boxPrepayCardService;

    @ApiOperation(value="获取预付金或服务卡订单")
    @GetMapping("/list")
    public Result list() {
        List<BoxPrepayCard> cardList = boxPrepayCardService.list(new QueryWrapper<BoxPrepayCard>().eq("deleted", 0).eq("status", 1));
        return Result.success().setData("cardList", cardList);
    }

}

