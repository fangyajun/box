package com.kuose.box.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.admin.order.dto.OrderGoodsDto;
import com.kuose.box.admin.order.service.BoxOrderGoodsService;
import com.kuose.box.db.goods.entity.BoxGoods;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import com.kuose.box.db.order.dao.BoxOrderGoodsMapper;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Service
public class BoxOrderGoodsServiceImpl extends ServiceImpl<BoxOrderGoodsMapper, BoxOrderGoods> implements BoxOrderGoodsService {

    @Autowired
    private BoxOrderGoodsMapper boxOrderGoodsMapper;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;
    @Autowired
    private BoxGoodsService boxGoodsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OrderGoodsDto orderGoodsDto) {
        Integer orderId = orderGoodsDto.getOrderId();
        Integer[] skuIds = orderGoodsDto.getSkuIds();
        // 先删除
        for (Integer skuId : skuIds) {
            boxOrderGoodsMapper.delete(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("sku_id", skuId));
        }
        // 在添加
        for (Integer skuId : skuIds) {
            BoxGoodsSku goodsSku = boxGoodsSkuService.getById(skuId);
            BoxGoods goods = boxGoodsService.getById(goodsSku.getGoodsId());

            BoxOrderGoods boxOrderGoods = new BoxOrderGoods();
            boxOrderGoods.setOrderId(orderId);
            boxOrderGoods.setGoodsId(goodsSku.getGoodsId());
            boxOrderGoods.setSkuId(skuId);
            boxOrderGoods.setGoodsName(goods.getName());
            boxOrderGoods.setGoodsNo(goods.getGoodsNo());
            boxOrderGoods.setSkuNo(goodsSku.getSkuCode());
            boxOrderGoods.setPrice(goodsSku.getRetailPrice());
            boxOrderGoods.setColorName(goodsSku.getColorName());
            boxOrderGoods.setSizeName(goodsSku.getSizeName());
            boxOrderGoods.setPicUrl(goods.getImg());
            boxOrderGoods.setOrderGoodsStatus(0);
            boxOrderGoods.setComment(0);
            boxOrderGoods.setAddTime(System.currentTimeMillis());
            boxOrderGoods.setUpdateTime(System.currentTimeMillis());

            boxOrderGoodsMapper.insert(boxOrderGoods);
        }
    }
}
