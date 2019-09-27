package com.kuose.box.admin.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.user.service.BoxUserService;
import com.kuose.box.db.user.dao.BoxUserMapper;
import com.kuose.box.db.user.entity.BoxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * box_user 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-04
 */
@Service
public class BoxUserServiceImpl extends ServiceImpl<BoxUserMapper, BoxUser> implements BoxUserService {

    @Autowired
    private BoxUserMapper boxUserMapper;

    @Override
    public IPage<BoxUser> listUser(Page<BoxUser> boxUserPage, BoxUser boxUser) {
        IPage<BoxUser> boxUserIPage = boxUserMapper.listUser(boxUserPage, boxUser.getMobile(), boxUser.getWeixinOpenid());
        return boxUserIPage;
    }
}
