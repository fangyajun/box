package com.kuose.box.admin.admin.controller;

import com.kuose.box.admin.admin.entity.BoxAdmin;
import com.kuose.box.admin.admin.service.BoxAdminService;
import com.kuose.box.admin.admin.service.BoxPermissionService;
import com.kuose.box.admin.admin.service.BoxRoleService;
import com.kuose.box.admin.log.service.impl.LogHelper;
import com.kuose.box.admin.util.Permission;
import com.kuose.box.admin.util.PermissionUtil;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.IpUtil;
import com.kuose.box.common.utils.JacksonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Api(tags = {"后台登录，接口文档"})
@RestController
@RequestMapping("/admin/auth")
@Validated
public class AdminAuthController {
    private final Log logger = LogFactory.getLog(AdminAuthController.class);

    @Autowired
    private BoxAdminService adminService;
    @Autowired
    private BoxRoleService roleService;
    @Autowired
    private BoxPermissionService permissionService;
    @Autowired
    private LogHelper logHelper;

    /**
     * 登录
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value="用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "登录名", required = true, dataType = "String", paramType = "form", example = "admin123"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form", example = "admin123")
    })
    @PostMapping("/login")
    public Object login(@RequestBody @ApiIgnore() String body, HttpServletRequest request) {
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.failure("用户名或密码为空");
        }

        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(new UsernamePasswordToken(username, password));
        } catch (UnknownAccountException uae) {
            logHelper.logAuthFail("登录", "用户帐号或密码不正确");
            return Result.failure("用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            logHelper.logAuthFail("登录", "用户帐号已锁定不可用");
            return Result.failure("用户帐号已锁定不可用");

        } catch (AuthenticationException ae) {
            logHelper.logAuthFail("登录", "认证失败");
            return Result.failure("认证失败");
        }

        currentUser = SecurityUtils.getSubject();
        BoxAdmin admin = (BoxAdmin) currentUser.getPrincipal();
        admin.setLastLoginIp(IpUtil.getIpAddr(request));
        admin.setLastLoginTime(System.currentTimeMillis());
        adminService.updateAdminById(admin);

        logHelper.logAuthSucceed("登录");

        // userInfo
        Map<String, Object> adminInfo = new HashMap<String, Object>();
        adminInfo.put("nickName", admin.getUsername());
        adminInfo.put("avatar", admin.getAvatar());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", currentUser.getSession().getId());
        result.put("adminInfo", adminInfo);
        return Result.success().setData("result", result);
    }

    /**
     * 退出登录
     * @return
     */
    @ApiOperation(value="退出登录")
    @RequiresAuthentication
    @PostMapping("/logout")
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();

        logHelper.logAuthSucceed("退出");
        currentUser.logout();
        return Result.success();
    }

    @ApiOperation(value="获取当前登录用户信息")
    @RequiresAuthentication
    @GetMapping("/info")
    public Object info() {
        Subject currentUser = SecurityUtils.getSubject();
        BoxAdmin admin = (BoxAdmin) currentUser.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("name", admin.getUsername());
        data.put("avatar", admin.getAvatar());

        Integer[] roleIds = admin.getRoleIds();
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        data.put("roles", roles);
        // NOTE
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        data.put("perms", toApi(permissions));
        return Result.success().setData("data", data);
    }

    @Autowired
    private ApplicationContext context;
    private HashMap<String, String> systemPermissionsMap = null;

    private Collection<String> toApi(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.kuose.box.admin";
            List<Permission> systemPermissions = PermissionUtil.listPermission(context, basicPackage);
            for (Permission permission : systemPermissions) {
                String perm = permission.getRequiresPermissions().value()[0];
                String api = permission.getApi();
                systemPermissionsMap.put(perm, api);
            }
        }

        Collection<String> apis = new HashSet<>();
        for (String perm : permissions) {
            String api = systemPermissionsMap.get(perm);
            apis.add(api);

            if (perm.equals("*")) {
                apis.clear();
                apis.add("*");
                return apis;
            }
        }
        return apis;
    }

    @ApiIgnore()
    @GetMapping("/401")
    public Object page401() {
        return Result.failure("请登录");
    }

    @ApiIgnore()
    @GetMapping("/index")
    public Object pageIndex() {
        return Result.success();
    }

    @ApiIgnore()
    @GetMapping("/403")
    public Object page403() {
        return Result.failure("无操作权限");
    }
}
