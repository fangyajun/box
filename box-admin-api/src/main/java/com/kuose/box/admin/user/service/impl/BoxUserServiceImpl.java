package com.kuose.box.admin.user.service.impl;

import com.kuose.box.admin.user.entity.BoxUser;
import com.kuose.box.admin.user.dao.BoxUserMapper;
import com.kuose.box.admin.user.service.BoxUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
