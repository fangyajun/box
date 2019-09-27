package com.kuose.box.wx.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.db.user.dao.BoxUserMapper;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.wx.login.service.BoxUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * box_user 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-09
 */
@Service
public class BoxUserServiceImpl extends ServiceImpl<BoxUserMapper, BoxUser> implements BoxUserService {

}
