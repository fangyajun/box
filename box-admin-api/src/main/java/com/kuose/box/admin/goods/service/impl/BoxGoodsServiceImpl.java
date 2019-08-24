package com.kuose.box.admin.goods.service.impl;

import com.kuose.box.admin.goods.dto.GoodsAllinone;
import com.kuose.box.admin.goods.entity.BoxGoods;
import com.kuose.box.admin.goods.dao.BoxGoodsMapper;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品基本信息表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Service
public class BoxGoodsServiceImpl extends ServiceImpl<BoxGoodsMapper, BoxGoods> implements BoxGoodsService {

    @Override
    public void save(GoodsAllinone goodsAllinone) {

    }
}
