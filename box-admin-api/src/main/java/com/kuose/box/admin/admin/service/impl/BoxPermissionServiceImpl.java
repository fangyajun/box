package com.kuose.box.admin.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.admin.entity.BoxPermission;
import com.kuose.box.admin.admin.dao.BoxPermissionMapper;
import com.kuose.box.admin.admin.service.BoxPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javafx.scene.shape.Box;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
@Service
public class BoxPermissionServiceImpl extends ServiceImpl<BoxPermissionMapper, BoxPermission> implements BoxPermissionService {

    @Autowired
    private BoxPermissionMapper boxPermissionMapper;

    @Override
    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }

        List<BoxPermission> boxPermissions = boxPermissionMapper.selectList(new QueryWrapper<BoxPermission>().in("role_id", Arrays.asList(roleIds)));

        for(BoxPermission permission : boxPermissions){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    @Override
    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }
        Integer selectCount = boxPermissionMapper.selectCount(new QueryWrapper<BoxPermission>().eq("role_id", roleId).eq("permission", "*").eq("deleted", 0));
        return selectCount != 0;
    }

    @Override
    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        List<BoxPermission> boxPermissions = boxPermissionMapper.selectList(new QueryWrapper<BoxPermission>().eq("role_id", roleId).eq("deleted", 0));
        for(BoxPermission permission : boxPermissions){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }
}
