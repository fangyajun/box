package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.db.order.dao.BoxOrderGoodsCommentMapper;
import com.kuose.box.db.order.dao.BoxOrderGoodsMapper;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.db.order.entity.BoxOrderGoodsComment;
import com.kuose.box.wx.order.service.BoxOrderGoodsService;
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
    private BoxOrderGoodsCommentMapper boxOrderGoodsCommentMapper;
    @Autowired
    private BoxOrderGoodsService boxOrderGoodsService;


    @Override
    @Transactional
    public void goodsAppraisement(BoxOrderGoodsComment boxOrderGoodsComment) {
        BoxOrderGoodsComment selectOne = boxOrderGoodsCommentMapper.selectOne(new QueryWrapper<BoxOrderGoodsComment>().
                eq("order_id", boxOrderGoodsComment.getOrderId()).eq("sku_id", boxOrderGoodsComment.getSkuId()));
        if (selectOne == null) {
            boxOrderGoodsComment.setAddTime(System.currentTimeMillis());
            boxOrderGoodsComment.setUpdateTime(System.currentTimeMillis());

            boxOrderGoodsCommentMapper.insert(boxOrderGoodsComment);
        } else {
            selectOne.setOrderGoodsStatus(boxOrderGoodsComment.getOrderGoodsStatus());
            selectOne.setSize(boxOrderGoodsComment.getSize());
            selectOne.setStyle(boxOrderGoodsComment.getStyle());
            selectOne.setMatch(boxOrderGoodsComment.getMatch());
            selectOne.setQuality(boxOrderGoodsComment.getQuality());
            selectOne.setPrice(boxOrderGoodsComment.getPrice());
            selectOne.setContent(boxOrderGoodsComment.getContent());
            selectOne.setStar(boxOrderGoodsComment.getStar());
            selectOne.setUpdateTime(System.currentTimeMillis());

            boxOrderGoodsCommentMapper.updateById(selectOne);
        }

        boxOrderGoodsService.update(new UpdateWrapper<BoxOrderGoods>().set("order_goods_status", boxOrderGoodsComment.getOrderGoodsStatus()).set("comment", boxOrderGoodsComment.getId())
                .eq("order_id", boxOrderGoodsComment.getOrderId()).eq("sku_id", boxOrderGoodsComment.getSkuId()).eq("deleted", 0));
    }
}
