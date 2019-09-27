package com.kuose.box.admin.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.db.user.entity.BoxUser;

/**
 * <p>
 * box_user 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-04
 */
public interface BoxUserService extends IService<BoxUser> {

    IPage<BoxUser> listUser(Page<BoxUser> boxUserPage, BoxUser boxUser);
}
