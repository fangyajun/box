package com.kuose.source.goods.service;

import com.kuose.source.goods.entity.AttributeSource;
import com.kuose.source.goods.entity.BoxGoods;
import com.kuose.source.goods.entity.RipreportProductinformation;
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
public interface RipreportProductinformationService extends IService<RipreportProductinformation> {

    List<BoxGoods> listGoods(String productno, String goodsName, String year);

    BoxGoods getGoods(String productno);

    List<AttributeSource> listAllAttibutes();
}
