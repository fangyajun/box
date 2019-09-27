package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.db.prepay.dao.BoxPrepayCardMapper;
import com.kuose.box.db.prepay.entity.BoxPrepayCard;
import com.kuose.box.wx.order.service.BoxPrepayCardService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 预付金或服务卡表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Service
public class BoxPrepayCardServiceImpl extends ServiceImpl<BoxPrepayCardMapper, BoxPrepayCard> implements BoxPrepayCardService {

}
