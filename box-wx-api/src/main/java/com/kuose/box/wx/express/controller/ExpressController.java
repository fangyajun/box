package com.kuose.box.wx.express.controller;

import com.kuose.box.common.config.Result;
import com.kuose.box.wx.express.entity.ExpressInfo;
import com.kuose.box.wx.express.service.ExpressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/10
 */
@Api(tags = {"物流信息"})
@RestController
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @ApiOperation(value="获取物流信息详情")
    @GetMapping("/getExpressDetail")
    public Result getExpressDetail(String expCode, String expNo) {
        ExpressInfo expressInfo = expressService.getExpressInfo(expCode, expNo);
        return Result.success().setData("expressInfo", expressInfo);
    }
}
