package com.kuose.box.admin.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeService;
import com.kuose.box.db.goods.dao.BoxGoodsAttributeMapper;
import com.kuose.box.db.goods.dto.GoodsAllinone;
import com.kuose.box.db.goods.entity.BoxGoodsAttribute;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品参数表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Service
public class BoxGoodsAttributeServiceImpl extends ServiceImpl<BoxGoodsAttributeMapper, BoxGoodsAttribute> implements BoxGoodsAttributeService {

    @Override
    public void save(GoodsAllinone goodsAllinone) {

    }
}
