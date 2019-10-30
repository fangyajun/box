package com.kuose.box.admin.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.admin.order.dto.OrderDto;
import com.kuose.box.admin.order.dto.ShipDTO;
import com.kuose.box.admin.order.service.BoxOrderGoodsService;
import com.kuose.box.admin.order.service.BoxOrderService;
import com.kuose.box.admin.prepay.service.BoxPrepayCardOrderService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import com.kuose.box.db.order.dao.BoxOrderMapper;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.order.entity.BoxOrderGoods;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
@Service
public class BoxOrderServiceImpl extends ServiceImpl<BoxOrderMapper, BoxOrder> implements BoxOrderService {
    @Autowired
    private BoxOrderMapper boxOrderMapper;
    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;
    @Autowired
    private BoxOrderGoodsService boxOrderGoodsService;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public IPage<BoxOrder> listOrderPage(Page<BoxOrder> boxOrderPage, OrderDto orderDto) {
        return boxOrderMapper.listOrderPage(boxOrderPage,orderDto.getOrderNo(), orderDto.getMinExpectTime(),
                orderDto.getMaxExpectTime(),orderDto.getMobile(), orderDto.getOrderStatus(), orderDto.getAuditStatus());
    }

    @Override
    public int updateWithOptimisticLocker(BoxOrder order) {
        Long updateTime = order.getUpdateTime();
        order.setUpdateTime(System.currentTimeMillis());
        QueryWrapper<BoxOrder> queryWrapper = new QueryWrapper<BoxOrder>().eq("id", order.getId()).eq("update_time", updateTime);
        return boxOrderMapper.update(order, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result ship(ShipDTO shipDTO) {
        BoxOrder order = boxOrderMapper.selectById(shipDTO.getOrderId());

        // 订单已发货就设置预付金服务中状态,可退预付金为0
        BoxPrepayCardOrder boxPrepayCardOrder = new BoxPrepayCardOrder();
        boxPrepayCardOrder.setId(order.getPrepayCardOrderId());
        // 预付金为不可退状态
        boxPrepayCardOrder.setRefund(0);
        boxPrepayCardOrder.setRefundPrepayAmounts(new BigDecimal(0));
        // 设置预付金订单状态为服务中
        boxPrepayCardOrder.setOrderStatus(3);
        boxPrepayCardOrderService.updateById(boxPrepayCardOrder);

        // 修改订单状态
        order.setOrderStatus(2);
        order.setShipSn(shipDTO.getShipSn());
        order.setShipChannel(shipDTO.getShipChannel());
        order.setShipTime(shipDTO.getShipTime());

        int update = updateWithOptimisticLocker(order);
        if (update == 0) {
            throw new RuntimeException("数据更新已失效");
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmUserBackGoods(Integer orderId) {
        // 更改订单状态
        BoxOrder boxOrder = boxOrderMapper.selectById(orderId);

        //  8-确认收到寄回商品，
        boxOrder.setOrderStatus(8);
        boxOrderMapper.updateById(boxOrder);

        // 预付金设置为可退状态
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(boxOrder.getPrepayCardOrderId());
        if (prepayCardOrder.getRefundPrepayAmounts().compareTo(new BigDecimal(0)) == 1) {
            // 0：不可退，1：可退， 2:已退
            prepayCardOrder.setRefund(1);
        }
        boxPrepayCardOrderService.updateById(prepayCardOrder);

        // 退回的商品库存加1
        List<BoxOrderGoods> goodsList = boxOrderGoodsService.list(new QueryWrapper<BoxOrderGoods>().eq("order_id", orderId).
                eq("deleted", 0).in("order_goods_status", 2, 3));
        if (goodsList != null && !goodsList.isEmpty()) {
            for (BoxOrderGoods boxOrderGoods : goodsList) {
                // 更新对应sku的库存数量
                BoxGoodsSku goodsSku = boxGoodsSkuService.getById(boxOrderGoods.getSkuId());
                goodsSku.setNumber(goodsSku.getNumber() + 1);
                boxGoodsSkuService.updateById(goodsSku);
            }
        }
    }

    @Override
    public Result refundPrePayMoney(Integer orderId) {
        BoxOrder boxOrder = boxOrderMapper.selectById(orderId);
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(boxOrder.getPrepayCardOrderId());
        if (prepayCardOrder.getRefund() == 1 && prepayCardOrder.getRefundPrepayAmounts().compareTo(new BigDecimal(0)) == 1) {
            // 退回预付金
            String refundPrePayMoneyUrl = "https://kuose.mynatapp.cc/payController/refundPrePayMoney?prepayCardOrderId=" + boxOrder.getPrepayCardOrderId();
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);
            ResponseEntity<JSONObject> exchange = restTemplate.exchange(refundPrePayMoneyUrl, HttpMethod.GET, httpEntity, JSONObject.class);
            JSONObject jsonResult = exchange.getBody();
            int status = jsonResult.getIntValue("status");
            if (status != 1) {
                return Result.failure(jsonResult.getString("info"));
            }
        }

        // 修改订单状态为已完成
        boxOrder.setOrderStatus(9);
        boxOrderMapper.updateById(boxOrder);

        return Result.success();
    }
}
