package com.kuose.box.wx.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.db.order.dao.BoxOrderGoodsCommentMapper;
import com.kuose.box.db.order.dao.BoxOrderGoodsMapper;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.db.order.entity.BoxOrderGoodsComment;
import com.kuose.box.wx.order.dto.OrderGoodsAppraisementDTO;
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
    @Transactional(rollbackFor = Exception.class)
    public void goodsAppraisement(OrderGoodsAppraisementDTO orderGoodsAppraisementDTO) {

        BoxOrderGoodsComment selectOne = boxOrderGoodsCommentMapper.selectOne(new QueryWrapper<BoxOrderGoodsComment>().
                eq("order_goods_id", orderGoodsAppraisementDTO.getOrderGoodsId()));

        BoxOrderGoods boxOrderGoods = boxOrderGoodsService.getById(orderGoodsAppraisementDTO.getOrderGoodsId());
        if (boxOrderGoods == null) {
            throw new RuntimeException("数据异常,根据订单商品id未能查到数据");
        }

        if (selectOne == null) {
            BoxOrderGoodsComment boxOrderGoodsComment = new BoxOrderGoodsComment();

            boxOrderGoodsComment.setOrderGoodsId(orderGoodsAppraisementDTO.getOrderGoodsId());
            boxOrderGoodsComment.setSkuId(boxOrderGoods.getSkuId());
            boxOrderGoodsComment.setUserId(orderGoodsAppraisementDTO.getUserId());
            boxOrderGoodsComment.setOrderGoodsStatus(orderGoodsAppraisementDTO.getOrderGoodsStatus());
            boxOrderGoodsComment.setSize(orderGoodsAppraisementDTO.getSize());
            boxOrderGoodsComment.setStyle(orderGoodsAppraisementDTO.getStyle());
            boxOrderGoodsComment.setMatch(orderGoodsAppraisementDTO.getMatch());
            boxOrderGoodsComment.setQuality(orderGoodsAppraisementDTO.getQuality());
            boxOrderGoodsComment.setPrice(orderGoodsAppraisementDTO.getPrice());
            boxOrderGoodsComment.setContent(orderGoodsAppraisementDTO.getContent());
            boxOrderGoodsComment.setStar(orderGoodsAppraisementDTO.getStar());
            boxOrderGoodsComment.setAddTime(System.currentTimeMillis());
            boxOrderGoodsComment.setUpdateTime(System.currentTimeMillis());

            boxOrderGoodsCommentMapper.insert(boxOrderGoodsComment);
        } else {
            selectOne.setOrderGoodsStatus(orderGoodsAppraisementDTO.getOrderGoodsStatus());
            selectOne.setSize(orderGoodsAppraisementDTO.getSize());
            selectOne.setStyle(orderGoodsAppraisementDTO.getStyle());
            selectOne.setMatch(orderGoodsAppraisementDTO.getMatch());
            selectOne.setQuality(orderGoodsAppraisementDTO.getQuality());
            selectOne.setPrice(orderGoodsAppraisementDTO.getPrice());
            selectOne.setContent(orderGoodsAppraisementDTO.getContent());
            selectOne.setStar(orderGoodsAppraisementDTO.getStar());
            selectOne.setUpdateTime(System.currentTimeMillis());

            boxOrderGoodsCommentMapper.updateById(selectOne);
        }

        boxOrderGoods.setOrderGoodsStatus(orderGoodsAppraisementDTO.getOrderGoodsStatus());
        boxOrderGoodsService.updateById(boxOrderGoods);
    }
}


