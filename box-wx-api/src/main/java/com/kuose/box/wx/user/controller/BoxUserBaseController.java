package com.kuose.box.wx.user.controller;

import com.kuose.box.common.config.Result;
import com.kuose.box.db.user.entity.BoxUserBase;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.user.service.BoxUserBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/4
 */
@Api(tags = {"用户信息 ，用户信息"})
@RestController
@RequestMapping("/BoxUserBase")
public class BoxUserBaseController {

    @Autowired
    private BoxUserBaseService boxUserBaseService;

    @ApiOperation(value="填写用户基本信息，设置期望收盒时间")
    @PostMapping("/add")
    public Result add(@RequestBody BoxUserBase boxUserBase, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        boxUserBase.setCreateTime(System.currentTimeMillis());
        boxUserBase.setUpdateTime(System.currentTimeMillis());
        boxUserBaseService.save(boxUserBase);
        return Result.success();
    }

    @ApiOperation(value="用户信息更新")
    @PostMapping("/update")
    public Result update(@RequestBody BoxUserBase boxUserBase, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }
        if (boxUserBase.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxUserBase.setUpdateTime(System.currentTimeMillis());
        boxUserBaseService.updateById(boxUserBase);
        return Result.success();
    }




}
