package com.kuose.box.admin.admin.service.impl;

import com.kuose.box.admin.admin.entity.BoxRole;
import com.kuose.box.admin.admin.dao.BoxRoleMapper;
import com.kuose.box.admin.admin.service.BoxRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
@Service
public class BoxRoleServiceImpl extends ServiceImpl<BoxRoleMapper, BoxRole> implements BoxRoleService {

    @Autowired
    private BoxRoleMapper boxRoleMapper;

    @Override
    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if(roleIds.length == 0){
            return roles;
        }

        List<BoxRole> boxRoles = boxRoleMapper.selectBatchIds(Arrays.asList(roleIds));

        for(BoxRole role : boxRoles){
            roles.add(role.getName());
        }

        return roles;

    }
}
