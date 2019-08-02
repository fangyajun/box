package com.kuose.box.admin.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.admin.entity.BoxAdmin;
import com.kuose.box.admin.admin.service.BoxAdminService;
import com.kuose.box.admin.annotation.RequiresPermissionsDesc;
import com.kuose.box.admin.log.service.impl.LogHelper;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.RegexUtil;
import com.kuose.box.common.utils.bcrypt.BCryptPasswordEncoder;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
@Api(tags = {"系统管理，管理员管理"})
@RestController
@RequestMapping("/boxAdmin")
public class BoxAdminController {


    @Autowired
    private BoxAdminService adminService;
    @Autowired
    private LogHelper logHelper;

    @ApiOperation(value="查询管理员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataType = "String", paramType = "path", example = "admin123"),
            @ApiImplicitParam(name = "page", value = "起始页", required = true, dataType = "Integer", paramType = "path", example = "1"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "path", example = "10")
    })
    @RequiresPermissions("admin:admin:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "查询")
    @GetMapping("/list")
    public Result list(String username, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Page<BoxAdmin> adminPage = new Page<>();
        adminPage.setSize(limit);
        adminPage.setCurrent(page);
        adminPage.setDesc("add_time");

        IPage<BoxAdmin> boxAdminIPage = adminService.listAdminsPage(adminPage, username);

        return Result.success().setData("boxAdminIPage", boxAdminIPage);
    }

    private Result validate(BoxAdmin admin) {
        String name = admin.getUsername();
        if (StringUtils.isEmpty(name)) {
            return Result.failure("缺少必须参数");
        }
        if (!RegexUtil.isUsername(name)) {
            return Result.failure("管理员名称不符合规定");
        }
        String password = admin.getPassword();
        if (StringUtils.isEmpty(password) || password.length() < 6) {
            return Result.failure("管理员密码长度不能小于6");
        }
        return null;
    }

    @ApiOperation(value="添加管理员")
    @RequiresPermissions("admin:admin:add")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "添加")
    @PostMapping("/add")
    public Result add(@RequestBody BoxAdmin admin) {
        Result error = validate(admin);
        if (error != null) {
            return error;
        }

        String username = admin.getUsername();
        List<BoxAdmin> adminList = adminService.list(new QueryWrapper<BoxAdmin>().eq("username", username));
        if (adminList.size() > 0) {
            return Result.failure("管理员已经存在");
        }

        String rawPassword = admin.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        admin.setPassword(encodedPassword);
        admin.setDeleted(0);
        admin.setAddTime(System.currentTimeMillis());
        admin.setUpdateTime(System.currentTimeMillis());
        adminService.saveBoxAdmin(admin);
        logHelper.logAuthSucceed("添加管理员", username);
        return Result.success().setData("admin", admin);
    }

    @ApiOperation(value="获取管理员详情")
    @RequiresPermissions("admin:admin:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "详情")
    @GetMapping("/read")
    public Result read(Integer id) {
        BoxAdmin boxAdmin = adminService.getById(id);
        return Result.success().setData("boxAdmin", boxAdmin);
    }

    @ApiOperation(value="修改管理员信息")
    @RequiresPermissions("admin:admin:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody BoxAdmin admin) {
//        Object error = validate(admin);
//        if (error != null) {
//            return error;
//        }

        Integer anotherAdminId = admin.getId();
        if (anotherAdminId == null) {
            return Result.failure("缺少必传参数");
        }

        // 不允许管理员通过编辑接口修改密码
        admin.setPassword(null);
        adminService.updateAdminById(admin);

        logHelper.logAuthSucceed("编辑管理员", admin.getUsername());
        return Result.success().setData("admin", admin);
    }

    @ApiOperation(value="删除管理员信息")
    @RequiresPermissions("admin:admin:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxAdmin admin) {
        Integer anotherAdminId = admin.getId();
        if (anotherAdminId == null) {
            return Result.failure("缺少必须参数");
        }

        // 管理员不能删除自身账号
        Subject currentUser = SecurityUtils.getSubject();
        BoxAdmin currentAdmin = (BoxAdmin) currentUser.getPrincipal();
        if (currentAdmin.getId().equals(anotherAdminId)) {
            return Result.failure("管理员不能删除自己账号");
        }

        // 逻辑删除
        admin.setDeleted(1);
        adminService.updateAdminById(admin);

        logHelper.logAuthSucceed("删除管理员", admin.getUsername());
        return Result.success();
    }

}

