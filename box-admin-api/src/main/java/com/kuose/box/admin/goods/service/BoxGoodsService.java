package com.kuose.box.admin.goods.service;

import com.kuose.box.admin.goods.dto.GoodsAllinone;
import com.kuose.box.admin.goods.entity.BoxGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品基本信息表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
public interface BoxGoodsService extends IService<BoxGoods> {

    void save(GoodsAllinone goodsAllinone);
}
