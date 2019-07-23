package com.kuose.box.wx.login.service.impl;

import com.kuose.box.wx.login.entity.BoxUser;
import com.kuose.box.wx.login.dao.BoxUserMapper;
import com.kuose.box.wx.login.service.BoxUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
