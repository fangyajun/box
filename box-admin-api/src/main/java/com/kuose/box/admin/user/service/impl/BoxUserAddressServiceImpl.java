package com.kuose.box.admin.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.user.service.BoxUserAddressService;
import com.kuose.box.db.user.dao.BoxUserAddressMapper;
import com.kuose.box.db.user.entity.BoxUserAddress;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收货地址表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Service
public class BoxUserAddressServiceImpl extends ServiceImpl<BoxUserAddressMapper, BoxUserAddress> implements BoxUserAddressService {

}
