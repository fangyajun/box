package com.kuose.box.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.admin.order.dto.OrderGoodsDto;
import com.kuose.box.admin.order.service.BoxOrderGoodsService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.goods.entity.BoxGoods;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import com.kuose.box.db.order.dao.BoxOrderGoodsMapper;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Result save(OrderGoodsDto orderGoodsDto) {
        Integer orderId = orderGoodsDto.getOrderId();
        Integer[] skuIds = orderGoodsDto.getSkuIds();

        // 先删除
        List<BoxOrderGoods> goodsList = boxOrderGoodsMapper.selectList(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("deleted", 0));
        if (goodsList != null && !goodsList.isEmpty()) {
            for (BoxOrderGoods boxOrderGoods : goodsList) {
                // 先删除，在更新对应sku的库存数量
                boxOrderGoodsMapper.delete(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).eq("sku_id", boxOrderGoods.getSkuId()));

                BoxGoodsSku goodsSku = boxGoodsSkuService.getById(boxOrderGoods.getSkuId());
                goodsSku.setNumber(goodsSku.getNumber() + 1);
                boxGoodsSkuService.updateById(goodsSku);
            }
        }

        // 在添加
        for (Integer skuId : skuIds) {
            BoxGoodsSku goodsSku = boxGoodsSkuService.getById(skuId);
            if (goodsSku.getNumber() < 1) {
                return Result.failure(506, "sku编号" + goodsSku.getSkuCode() + "库存不足");
            }
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
            boxOrderGoods.setPicUrl(goods.getOosImg());

            // 默认保留'盒子商品状态，1：保留，2：退货，3：换货',
            boxOrderGoods.setOrderGoodsStatus(1);
            boxOrderGoods.setComment(0);
            boxOrderGoods.setAddTime(System.currentTimeMillis());
            boxOrderGoods.setUpdateTime(System.currentTimeMillis());

            boxOrderGoodsMapper.insert(boxOrderGoods);

            // 减掉对应sku的库存
            goodsSku.setNumber(goodsSku.getNumber() - 1);
            boxGoodsSkuService.updateById(goodsSku);
        }
        return Result.success();
    }
}
