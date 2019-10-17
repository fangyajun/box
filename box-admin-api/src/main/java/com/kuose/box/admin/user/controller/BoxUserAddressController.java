package com.kuose.box.admin.user.controller;


import com.kuose.box.admin.user.service.BoxUserAddressService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.user.entity.BoxUserAddress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @ApiOperation(value="获取用户的收货地址")
    @GetMapping("/getUserAddr")
    public Result getUserAddr(Integer id) {
        if (id == null) {
            return Result.failure("缺少必传参数");
        }

        BoxUserAddress userAddress = boxUserAddressService.getById(id);
        return Result.success().setData("userAddress", userAddress);
    }

}

