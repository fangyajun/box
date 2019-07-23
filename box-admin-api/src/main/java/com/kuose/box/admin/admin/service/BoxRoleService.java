package com.kuose.box.admin.admin.service;

import com.kuose.box.admin.admin.entity.BoxRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
public interface BoxRoleService extends IService<BoxRole> {

    Set<String> queryByIds(Integer[] roleIds);
}
