package com.kuose.box.wx.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.wx.order.entity.BoxUserAddress;
import com.kuose.box.wx.order.service.BoxUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 收货地址表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-25
 */
@Api(tags = {"收货地址 ，用户收货地址"})
@RestController
@RequestMapping("/boxUserAddress")
public class BoxUserAddressController {

    @Autowired
    private BoxUserAddressService boxUserAddressService;

    @ApiOperation(value="新增收货地址")
    @PostMapping("/add")
    public Result add(@RequestBody BoxUserAddress boxUserAddress) {
        if (boxUserAddress.getId() == null) {
            return Result.failure("用户id不能为空");
        }
        if (StringUtil.isBlank(boxUserAddress.getName()) || StringUtil.isBlank(boxUserAddress.getPhone()) || StringUtil.isBlank(boxUserAddress.getAddressDetail())) {
            return Result.failure("收货人，收货详细地址或者收货手机号码为空");
        }

        boxUserAddress.setAddTime(System.currentTimeMillis());
        boxUserAddress.setUpdateTime(System.currentTimeMillis());

        boxUserAddressService.save(boxUserAddress);
        return Result.success();
    }

    @ApiOperation(value="修改收货地址")
    @PostMapping("/update")
    public Result update(@RequestBody BoxUserAddress boxUserAddress) {
        if (boxUserAddress.getId() == null) {
            return Result.failure("缺少必传参数");
        }
        boxUserAddress.setUpdateTime(System.currentTimeMillis());

        boxUserAddressService.save(boxUserAddress);
        return Result.success();
    }

    @ApiOperation(value="收货地址列表")
    @GetMapping("/list")
    public Result list(Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        List<BoxUserAddress> userAddressList = boxUserAddressService.list(new QueryWrapper<BoxUserAddress>().
                eq("deleted", 0).eq("user_id", userId).orderByDesc("is_default"));

        return Result.success().setData("userAddressList", userAddressList);
    }

    @ApiOperation(value="删除收货地址")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxUserAddress boxUserAddress) {
        if (boxUserAddress.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxUserAddress.setDeleted(1);
        boxUserAddressService.updateById(boxUserAddress);
        return Result.success();
    }





}

