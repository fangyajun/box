package com.kuose.source.goods.service;

import com.kuose.source.goods.entity.BoxGoodsSku;
import com.kuose.source.goods.entity.RipreportProductinformationsku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
public interface RipreportProductinformationskuService extends IService<RipreportProductinformationsku> {

    List<BoxGoodsSku> listGoodsSku(String goodsNo);
}
