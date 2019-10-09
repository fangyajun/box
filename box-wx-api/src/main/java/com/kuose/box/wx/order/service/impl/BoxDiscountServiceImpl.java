package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.db.discount.dao.BoxDiscountMapper;
import com.kuose.box.db.discount.entity.BoxDiscount;
import com.kuose.box.wx.order.service.BoxDiscountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 盒子折扣率 服务实现类
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-10-07
 */
@Service
public class BoxDiscountServiceImpl extends ServiceImpl<BoxDiscountMapper, BoxDiscount> implements BoxDiscountService {

}
