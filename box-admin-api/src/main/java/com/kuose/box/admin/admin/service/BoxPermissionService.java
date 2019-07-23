package com.kuose.box.admin.admin.service;

import com.kuose.box.admin.admin.entity.BoxPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
public interface BoxPermissionService extends IService<BoxPermission> {

    Set<String> queryByRoleIds(Integer[] roleIds);
}
