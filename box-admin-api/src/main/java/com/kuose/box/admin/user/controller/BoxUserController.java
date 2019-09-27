package com.kuose.box.admin.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.user.service.BoxUserBaseService;
import com.kuose.box.admin.user.service.BoxUserService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.db.user.entity.BoxUserBase;
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
@Api(tags = {"用户管理，用户管理"})
@RestController
@RequestMapping("/boxUser")
public class BoxUserController {

    @Autowired
    private BoxUserService boxUserService;
    @Autowired
    private BoxUserBaseService boxUserBaseService;

    @ApiOperation(value="获取用户详情")
    @GetMapping("/getUser")
    public Result getUser(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }
        BoxUser boxUser = boxUserService.getOne(new QueryWrapper<BoxUser>().eq("deleted", 0 ).eq("id", userId));
        boxUser.setPassword(null);
        BoxUserBase boxUserBase = boxUserBaseService.getOne(new QueryWrapper<BoxUserBase>().eq("deleted", 0).eq("user_id", userId));
        return Result.success().setData("boxUser",boxUser).setData("boxUserBase",boxUserBase);
    }

    @ApiOperation(value="用户列表")
    @GetMapping("/listUser")
    public Result listUser(BoxUser boxUser, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit){
        Page<BoxUser> boxUserPage = new Page<>();
        boxUserPage.setSize(limit);
        boxUserPage.setCurrent(page);

        IPage<BoxUser> boxUserIPage = boxUserService.listUser(boxUserPage, boxUser);
        return Result.success().setData("boxUserIPage", boxUserIPage);
    }

    @ApiOperation(value="修改用户信息")
    @PostMapping("/update")
    public Result update(@RequestBody BoxUserBase boxUserBase) {
        if (boxUserBase.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxUserBaseService.update(boxUserBase, new UpdateWrapper<BoxUserBase>().eq("user_id", boxUserBase.getUserId()).eq("deleted", 0));
        return Result.success();
    }

    @ApiOperation(value="添加用户基础信息")
    @PostMapping("/addUserBase")
    public Result addUserBase(@RequestBody BoxUserBase boxUserBase) {
        if (boxUserBase.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxUserBase.setCreateTime(System.currentTimeMillis());
        boxUserBase.setUpdateTime(System.currentTimeMillis());

        boxUserBaseService.save(boxUserBase);
        return Result.success();
    }

    @ApiOperation(value="添加用户信息")
    @PostMapping("/addUser")
    public Result addUser(@RequestBody BoxUser boxUser) {
        boxUser.setUpdateTime(System.currentTimeMillis());
        boxUser.setCreateTime(System.currentTimeMillis());
        boxUserService.save(boxUser);
        return Result.success();
    }

}

