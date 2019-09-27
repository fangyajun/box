package com.kuose.box.admin.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.user.service.BoxUserBaseService;
import com.kuose.box.db.user.dao.BoxUserBaseMapper;
import com.kuose.box.db.user.entity.BoxUserBase;
import org.springframework.stereotype.Service;

/**
 * <p>
 * box_user_base 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-09
 */
@Service
public class BoxUserBaseServiceImpl extends ServiceImpl<BoxUserBaseMapper, BoxUserBase> implements BoxUserBaseService {

}
