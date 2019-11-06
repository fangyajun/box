package com.kuose.box.admin.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.admin.recommend.dto.RecommendGoodsDTO;
import com.kuose.box.admin.recommend.service.BoxRecommendGoodsService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.goods.entity.BoxGoods;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import com.kuose.box.db.recommend.dao.BoxRecommendGoodsMapper;
import com.kuose.box.db.recommend.entity.BoxRecommendGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户推荐商品商品表 服务实现类
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Service
public class BoxRecommendGoodsServiceImpl extends ServiceImpl<BoxRecommendGoodsMapper, BoxRecommendGoods> implements BoxRecommendGoodsService {
    @Autowired
    private BoxRecommendGoodsMapper boxRecommendGoodsMapper;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;
    @Autowired
    private BoxGoodsService boxGoodsService;



    @Override
    public Result save(RecommendGoodsDTO recommendGoodsDTO) {
        Integer boxRecommendId = recommendGoodsDTO.getBoxRecommendId();
        Integer[] skuIds = recommendGoodsDTO.getSkuIds();

        List<BoxRecommendGoods> recommendGoodsList = boxRecommendGoodsMapper.selectList(new QueryWrapper<BoxRecommendGoods>().eq("box_recommend_id", boxRecommendId).eq("deleted", 0));
        if (recommendGoodsList != null && !recommendGoodsList.isEmpty()) {
            // 删除
            boxRecommendGoodsMapper.delete(new QueryWrapper<BoxRecommendGoods>().eq("box_recommend_id", boxRecommendId));
        }

        // 添加
        for (Integer skuId : skuIds) {
            BoxGoodsSku goodsSku = boxGoodsSkuService.getById(skuId);
            BoxGoods goods = boxGoodsService.getById(goodsSku.getGoodsId());

            BoxRecommendGoods boxRecommendGoods = new BoxRecommendGoods();
            boxRecommendGoods.setBoxRecommendId(boxRecommendId);
            boxRecommendGoods.setGoodsId(goodsSku.getGoodsId());
            boxRecommendGoods.setSkuId(skuId);
            boxRecommendGoods.setGoodsName(goods.getName());
            boxRecommendGoods.setGoodsNo(goods.getGoodsNo());
            boxRecommendGoods.setSkuNo(goodsSku.getSkuCode());
            boxRecommendGoods.setPrice(goodsSku.getRetailPrice());
            boxRecommendGoods.setColorName(goodsSku.getColorName());
            boxRecommendGoods.setSizeName(goodsSku.getSizeName());
            boxRecommendGoods.setTmallUrl(goods.getTmallUrl());
            boxRecommendGoods.setPicUrl(goods.getImg());
            boxRecommendGoods.setCreateTime(System.currentTimeMillis());
            boxRecommendGoods.setUpdateTime(System.currentTimeMillis());

            boxRecommendGoodsMapper.insert(boxRecommendGoods);
        }

        return Result.success();
    }
}
