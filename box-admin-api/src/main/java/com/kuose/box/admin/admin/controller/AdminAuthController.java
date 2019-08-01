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
import io.swagger.annotations.Api;
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


/**
 * @author 魔舞清华
 */
@Api(tags = {"后台登录"})
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
     * @param
     * @param request
     * @return
     */
    @ApiOperation(value="用户登录")
    @PostMapping("/login")
    public Object login(@RequestBody BoxAdmin boxAdmin, HttpServletRequest request) {
        String username = boxAdmin.getUsername();
        String password = boxAdmin.getPassword();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.failure(601,"用户名或密码为空");
        }

        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(new UsernamePasswordToken(username, password));
        } catch (UnknownAccountException uae) {
            logHelper.logAuthFail("登录", "用户帐号或密码不正确");
            return Result.failure(601, "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            logHelper.logAuthFail("登录", "用户帐号已锁定不可用");
            return Result.failure(601 ,"用户帐号已锁定不可用");

        } catch (AuthenticationException ae) {
            logHelper.logAuthFail("登录", "认证失败");
            return Result.failure(601, "认证失败");
        }

        currentUser = SecurityUtils.getSubject();
        // 设置登录过期时间 单位毫秒
         currentUser.getSession().setTimeout(3600*1000*24);
        BoxAdmin admin = (BoxAdmin) currentUser.getPrincipal();
        admin.setLastLoginIp(IpUtil.getIpAddr(request));
        admin.setLastLoginTime(System.currentTimeMillis());
        adminService.updateAdminById(admin);

        logHelper.logAuthSucceed("登录");

        // userInfo
        Map<String, Object> adminInfo = new HashMap(2);
        adminInfo.put("nickName", admin.getUsername());
        adminInfo.put("avatar", admin.getAvatar());

        return Result.success().setData("token", currentUser.getSession().getId()).setData("adminInfo", adminInfo);
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
    public Result info() {
        Subject currentUser = SecurityUtils.getSubject();
        BoxAdmin admin = (BoxAdmin) currentUser.getPrincipal();

        Integer[] roleIds = admin.getRoleIds();
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        HashMap<String, Object> rolesIfo = new HashMap<>();
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        admin.setPassword(null);
        return Result.success().setData("adminInfo", admin).setData("permissions", toApi(permissions)).setData("roles", roles);
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
        return Result.failure(501 ,"未登录或登录已过期，请重新登录");
    }

    @ApiIgnore()
    @GetMapping("/index")
    public Object pageIndex() {
        return Result.success();
    }

    @ApiIgnore()
    @GetMapping("/403")
    public Object page403() {
        return Result.failure(506,"无操作权限");
    }
}
