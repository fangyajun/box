package com.kuose.box.wx.pay.service;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.IpUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.wx.order.service.BoxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/11
 */
@Service
public class PayService {

    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private WxPayService wxPayService;

    /**
     * 付款订单的预支付会话标识
     * <p>
     * 1. 检测当前订单是否能够付款
     * 2. 微信商户平台返回支付订单ID
     * 3. 设置订单付款状态
     *
     * @param orderId
     * @param openid
     * @return 支付订单ID
     */
    @Transactional
    public Result prepay(Integer orderId, String openid, HttpServletRequest request) {
        WxPayMpOrderResult result = null;
        try {
            BoxOrder order = boxOrderService.getById(orderId);
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();

            orderRequest.setOutTradeNo(order.getOrderNo());
            orderRequest.setOpenid(openid);
            orderRequest.setBody("订单：" + order.getOrderNo());
            // 元转成分
            int fee = 0;
            // 实付费用转换成分
            BigDecimal actualPrice = order.getActualPrice();
            fee = actualPrice.multiply(new BigDecimal(100)).intValue();

            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));

            result = wxPayService.createOrder(orderRequest);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(506, "内部异常，订单不能支付");
        }

//        if (orderService.updateWithOptimisticLocker(order) == 0) {
//            return ResponseUtil.updatedDateExpired();
//        }
        return Result.success().setData("result", result);


    }
}
