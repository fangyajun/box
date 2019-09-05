package com.kuose.box.admin.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.goods.dto.GoodsAllinone;
import com.kuose.box.admin.goods.dto.GoodsQueryParameter;
import com.kuose.box.admin.goods.dto.GoodsSkuVo;
import com.kuose.box.admin.goods.entity.BoxGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.common.config.Result;

/**
 * <p>
 * 商品基本信息表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
public interface BoxGoodsService extends IService<BoxGoods> {

    Result save(GoodsAllinone goodsAllinone);

    void deleteGoods(BoxGoods boxGoods);

    IPage<BoxGoods> listGoodsPage(Page<BoxGoods> adminPage, GoodsQueryParameter goodsQueryParameter);

    Result update(GoodsAllinone goodsAllinone);

    IPage<GoodsSkuVo> listGoodsAndSku(Page<BoxGoods> boxGoodsPage, GoodsQueryParameter goodsQueryParameter);
}
