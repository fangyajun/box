package com.kuose.box.admin.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.admin.entity.BoxAdmin;
import com.kuose.box.admin.admin.entity.BoxPermission;
import com.kuose.box.admin.admin.entity.BoxRole;
import com.kuose.box.admin.admin.service.BoxAdminService;
import com.kuose.box.admin.admin.service.BoxPermissionService;
import com.kuose.box.admin.admin.service.BoxRoleService;
import com.kuose.box.admin.annotation.RequiresPermissionsDesc;
import com.kuose.box.admin.util.Permission;
import com.kuose.box.admin.util.PermissionUtil;
import com.kuose.box.admin.vo.PermVo;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.JacksonUtil;
import com.kuose.box.common.utils.PinYinUtils;
import io.swagger.annotations.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author 魔舞清华
 */
@Api(tags = {"系统管理，角色管理"})
@RestController
@RequestMapping("boxRole")
@Validated
public class BoxRoleController {
    private final Log logger = LogFactory.getLog(BoxRoleController.class);

    @Autowired
    private BoxRoleService roleService;
    @Autowired
    private BoxPermissionService permissionService;
    @Autowired
    private BoxAdminService adminService;

    @ApiOperation(value="角色查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "起始页", required = true, dataType = "Integer", paramType = "path", example = "1"),
            @ApiImplicitParam(name = "limit", value = "每页条数", required = true, dataType = "Integer", paramType = "path", example = "10")
    })
    @RequiresPermissions("admin:role:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色查询")
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Page<BoxRole> rolePage = new Page<>();
        rolePage.setSize(limit);
        rolePage.setCurrent(page);
        rolePage.setDesc("add_time");

        IPage<BoxRole> boxRoleIPage = roleService.listRolePage(rolePage);
        return Result.success().setData("boxRoleIPage", boxRoleIPage);
    }

    @ApiOperation(value="获取所有的角色信息")
    @GetMapping("/options")
    public Result options() {
        List<BoxRole> roleList = roleService.list();

        List<Map<String, Object>> options = new ArrayList<>(roleList.size());
        for (BoxRole role : roleList) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", role.getId());
            option.put("label", role.getRoleName());
            options.add(option);
        }

        return Result.success().setData("options", options);
    }

    @ApiOperation(value="根据id获取角色详情")
    @RequiresPermissions("admin:role:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        BoxRole role = roleService.getById(id);
        return Result.success().setData("role", role);
    }


    private Result validate(BoxRole role) {
        String name = role.getRoleName();
        if (StringUtils.isEmpty(name)) {
            return Result.failure("参数不合法");
        }

        return null;
    }

    @ApiOperation(value="角色添加")
    @RequiresPermissions("admin:role:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色添加")
    @PostMapping("/create")
    public Object create(@RequestBody BoxRole role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }
        role.setSignName(PinYinUtils.changeToGetShortPinYin(role.getRoleName()).toUpperCase());

        List<BoxRole> roleList = roleService.list(new QueryWrapper<BoxRole>().eq("role_name", role.getRoleName()));
        if (roleList != null && roleList.size() != 0) {
            return Result.failure("角色名称已经存在");
        }

        roleService.save(role);
        return Result.success().setData("role", role);
    }

    @ApiOperation(value="根据id修改角色")
    @RequiresPermissions("admin:role:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色编辑")
    @PostMapping("/update")
    public Object update(@RequestBody BoxRole role) {
        if (role.getId() == null) {
            return Result.failure("缺少必传参数");
        }
        Object error = validate(role);
        if (error != null) {
            return error;
        }

        roleService.updateById(role);
        return Result.success();
    }

    @ApiOperation(value="根据id删除角色")
    @RequiresPermissions("admin:role:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色删除")
    @PostMapping("/delete")
    public Object delete(@RequestParam("id") Integer id) {
        if (id == null) {
            return Result.failure("缺少必传参数");
        }

        // 如果当前角色所对应管理员仍存在，则拒绝删除角色。
        List<BoxAdmin> adminList = adminService.list();
        for (BoxAdmin admin : adminList) {
            Integer[] roleIds = admin.getRoleIds();
            for (Integer roleId : roleIds) {
                if (id.equals(roleId)) {
                    return Result.failure("当前角色存在管理员，不能删除");
                }
            }
        }

        BoxRole boxRole = new BoxRole();
        boxRole.setId(id);
        // 逻辑删除
        boxRole.setDeleted(true);
        roleService.updateById(boxRole);

        return Result.success();
    }


    @Autowired
    private ApplicationContext context;
    private List<PermVo> systemPermissions = null;
    private Set<String> systemPermissionsString = null;

    private List<PermVo> getSystemPermissions() {
        final String basicPackage = "com.kuose.box.admin";
        if (systemPermissions == null) {
            List<Permission> permissions = PermissionUtil.listPermission(context, basicPackage);
            systemPermissions = PermissionUtil.listPermVo(permissions);
            systemPermissionsString = PermissionUtil.listPermissionString(permissions);
        }
        return systemPermissions;
    }

    private Set<String> getAssignedPermissions(Integer roleId) {
        // 这里需要注意的是，如果存在超级权限*，那么这里需要转化成当前所有系统权限。
        // 之所以这么做，是因为前端不能识别超级权限，所以这里需要转换一下。
        Set<String> assignedPermissions = null;
        if (permissionService.checkSuperPermission(roleId)) {
            getSystemPermissions();
            assignedPermissions = systemPermissionsString;
        } else {
            assignedPermissions = permissionService.queryByRoleId(roleId)
            ;
        }

        return assignedPermissions;
    }

    /**
     * 管理员的权限情况
     *
     * @return 系统所有权限列表和管理员已分配权限
     */
    @ApiOperation(value="角色的权限信息")
    @RequiresPermissions("admin:role:permission:get")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "权限详情")
    @GetMapping("/getPermissions")
    public Object getPermissions(Integer roleId) {
        List<PermVo> systemPermissions = getSystemPermissions();
        Set<String> assignedPermissions = getAssignedPermissions(roleId);

        Map<String, Object> data = new HashMap<>();
        data.put("systemPermissions", systemPermissions);
        data.put("assignedPermissions", assignedPermissions);
        return Result.success().setData("data", data);
    }


    /**
     * 更新管理员的权限
     *
     * @param body
     * @return
     *
     * admin:storage:update
     * admin:storage:delete
     * admin:storage:read
     * admin:storage:create
     * admin:storage:list
     */
    @ApiOperation(value="更新角色的权限")
    @RequiresPermissions("admin:role:permission:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "权限变更")
    @PostMapping("/updatePermissions")
    public Object updatePermissions(@ApiParam(name="body",value="json数据,例：{'roleId':1, permissions:['admin:admin:update','admin:admin:delete','admin:storage:create']}")
                                    @RequestBody String body) {
        Integer roleId = JacksonUtil.parseInteger(body, "roleId");
        List<String> permissions = JacksonUtil.parseStringList(body, "permissions");
        if (roleId == null || permissions == null) {
            return Result.failure("缺少必传参数");
        }

        // 如果修改的角色是超级权限，则拒绝修改。
        if (permissionService.checkSuperPermission(roleId)) {
            return Result.failure("当前角色的超级权限不能变更");
        }

        // 先删除旧的权限，再更新新的权限
        permissionService.remove(new QueryWrapper<BoxPermission>().eq("role_id", roleId));
        for (String permission : permissions) {
            BoxPermission boxPermission = new BoxPermission();
            boxPermission.setAddTime(System.currentTimeMillis());
            boxPermission.setUpdateTime(System.currentTimeMillis());
            boxPermission.setRoleId(roleId);
            boxPermission.setPermission(permission);
            permissionService.save(boxPermission);
        }
        return Result.success();
    }

}
