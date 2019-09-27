package com.kuose.box.admin.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.db.goods.dto.GoodsAllinone;
import com.kuose.box.db.goods.entity.BoxGoodsAttribute;

/**
 * <p>
 * 商品参数表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
public interface BoxGoodsAttributeService extends IService<BoxGoodsAttribute> {

    void save(GoodsAllinone goodsAllinone);
}
