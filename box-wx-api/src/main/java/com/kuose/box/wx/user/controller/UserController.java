package com.kuose.box.wx.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.wx.login.service.BoxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * box_user 前端控制器
 * </p>
 * @author fangyajun
 * @since 2019-09-04
 */
@Api(tags = {"用户信息"})
@RestController
@RequestMapping("/userController")
public class UserController {

    @Autowired
    private BoxUserService boxUserService;


    @ApiOperation(value="获取用户全身照和补充爱好信息")
    @GetMapping("/getUser")
    public Result getUser(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxUser boxUser = boxUserService.getOne(new QueryWrapper<BoxUser>().select("full_size_pic", "supplement_hobby").eq("id", userId));

        return Result.success().setData("boxUser",boxUser);
    }



    @ApiOperation(value="修改用户信息,用户全身照和补充信息说明")
    @PostMapping("/update")
    public Result update(@RequestBody BoxUser boxUser) {
        if (boxUser.getId() == null) {
            return Result.failure("缺少必传参数");
        }
        BoxUser updateBoxUser = new BoxUser();
        updateBoxUser.setId(boxUser.getId());
        updateBoxUser.setFullSizePic(boxUser.getFullSizePic());
        updateBoxUser.setSupplementHobby(boxUser.getSupplementHobby());
        updateBoxUser.setUpdateTime(System.currentTimeMillis());

        boxUserService.updateById(updateBoxUser);
        return Result.success();
    }

}

