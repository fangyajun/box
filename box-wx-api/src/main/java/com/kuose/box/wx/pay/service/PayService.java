package com.kuose.box.wx.pay.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.IpUtil;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.wx.order.service.BoxOrderService;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/11
 */
@Service
public class PayService {

    private final Log logger = LogFactory.getLog(PayService.class);

    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;

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
    @Transactional(rollbackFor = Exception.class)
    public Result prepayOrder(Integer orderId, String openid, HttpServletRequest request) {
        WxPayMpOrderResult result = null;
        BoxOrder order = boxOrderService.getById(orderId);
        try {
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

        if (boxOrderService.updateWithOptimisticLocker(order) == 0) {
            return Result.failure(508, "更新数据已失效");
        }
        return Result.success().setData("result", result);
    }

    /**
     * 预付金预支付会话标识
     * <p>
     * 1. 检测当前订单是否能够付款
     * 2. 微信商户平台返回支付订单ID
     * 3. 设置订单付款状态
     *
     * @param orderId
     * @param openid
     * @return 支付订单ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Result prepayCardOrder(Integer orderId, String openid, HttpServletRequest request) {
        WxPayMpOrderResult result = null;

        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(orderId);
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();

            orderRequest.setOutTradeNo(prepayCardOrder.getOrderNo());
            orderRequest.setOpenid(openid);
            orderRequest.setBody("订单：" + prepayCardOrder.getOrderNo());
            // 元转成分
            int fee = 0;
            // 实付费用转换成分
            BigDecimal actualPrice = prepayCardOrder.getOrderPrice();
            fee = actualPrice.multiply(new BigDecimal(100)).intValue();

            orderRequest.setTotalFee(fee);
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));

            result = wxPayService.createOrder(orderRequest);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure(506, "内部异常，订单不能支付");
        }

        return Result.success().setData("result", result);
    }


    /**
     * 微信付款成功或失败回调接口
     * <p>
     * 1. 检测当前订单是否是付款状态;
     * 2. 设置订单付款成功状态相关信息;
     * 3. 响应微信商户平台.
     *
     * @param request  请求内容
     * @param response 响应内容
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        String xmlResult = null;
        try {
            xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        } catch (IOException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        WxPayOrderNotifyResult result = null;
        try {
            result = wxPayService.parseOrderNotifyResult(xmlResult);

            if(!WxPayConstants.ResultCode.SUCCESS.equals(result.getResultCode())){
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
            if(!WxPayConstants.ResultCode.SUCCESS.equals(result.getReturnCode())){
                logger.error(xmlResult);
                throw new WxPayException("微信通知支付失败！");
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        logger.info("处理腾讯支付平台的订单支付");
        logger.info(result);

        String orderSn = result.getOutTradeNo();
        String payId = result.getTransactionId();

        // 分转化成元
        String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getOne(new QueryWrapper<BoxPrepayCardOrder>().eq("order_no", orderSn));
        BoxOrder boxOrder = boxOrderService.getOne(new QueryWrapper<BoxOrder>().eq("order_no", orderSn));
        if (prepayCardOrder == null && boxOrder == null) {
            return WxPayNotifyResponse.fail("订单不存在 sn=" + orderSn);
        }

        // 是预付金订单付款成功回调
        if (prepayCardOrder != null) {
            // 检查这个订单是否已经处理过
            if (!StringUtil.isBlank(prepayCardOrder.getPayId()) && prepayCardOrder.getPayTime() != null) {
                    return WxPayNotifyResponse.success("订单已经处理成功!");
            }
            // 检查支付订单金额
            if (!totalFee.equals(prepayCardOrder.getOrderPrice().toString())) {
                return WxPayNotifyResponse.fail(prepayCardOrder.getOrderNo() + " : 支付金额不符合 totalFee=" + totalFee);
            }

            prepayCardOrder.setRefund(1);
            prepayCardOrder.setRefundPrepayAmounts(prepayCardOrder.getOrderPrice());
            prepayCardOrder.setPayId(payId);
            prepayCardOrder.setPayTime(System.currentTimeMillis());
            prepayCardOrder.setOrderStatus(2);

            int update = boxPrepayCardOrderService.updateWithOptimisticLocker(prepayCardOrder);
            if (update == 0) {
                return WxPayNotifyResponse.fail("更新数据已失效");
            }

        } else {
            // 是盒子订单，处理成功后的逻辑
            if (!StringUtil.isBlank(boxOrder.getPayId()) && boxOrder.getPayTime() != null) {
                return WxPayNotifyResponse.success("订单已经处理成功!");
            }

            // 检查支付订单金额
            if (!totalFee.equals(boxOrder.getActualPrice().toString())) {
                return WxPayNotifyResponse.fail(prepayCardOrder.getOrderNo() + " : 支付金额不符合 totalFee=" + totalFee);
            }

            // 更新预付金订单可退款金额
            BoxPrepayCardOrder cardOrder = boxPrepayCardOrderService.getById(boxOrder.getPrepayCardOrderId());
            cardOrder.setRefundPrepayAmounts(boxOrder.getRefundPrepayAmounts());
            boxPrepayCardOrderService.updateById(cardOrder);

            boxOrder.setPayId(payId);
            boxOrder.setPayTime(System.currentTimeMillis());
            boxOrder.setOrderStatus(5);




            int update = boxOrderService.updateWithOptimisticLocker(boxOrder);
            if (update == 0) {
                return WxPayNotifyResponse.fail("更新数据已失效");
            } else {
                return WxPayNotifyResponse.success("处理成功!");
            }
        }
        return WxPayNotifyResponse.success("处理成功!");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result refundPrePayCard(Integer prepayCardOrderId) {
        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(prepayCardOrderId);
        if (prepayCardOrder == null) {
            return Result.failure(506, "数据异常，查无此预付金订单");
        }

        if (prepayCardOrder.getRefund() != 1) {
            return Result.failure(506, "预付款不是可退状态，无法退款");
        }

        if (prepayCardOrder.getRefundPrepayAmounts().compareTo(new BigDecimal(0)) == 0) {
            return Result.failure(506, "可退金额为0");
        }

        // 微信退款
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(prepayCardOrder.getOrderNo());
        wxPayRefundRequest.setOutRefundNo("refund_" + prepayCardOrder.getOrderNo());
        // 元转成分
        Integer totalFee = prepayCardOrder.getRefundPrepayAmounts().multiply(new BigDecimal(100)).intValue();
        wxPayRefundRequest.setTotalFee(totalFee);
        wxPayRefundRequest.setRefundFee(totalFee);

        WxPayRefundResult wxPayRefundResult;
        try {
            wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            logger.error(e.getMessage(), e);
            return Result.failure(507, "订单退款失败");
        }
        if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return Result.failure(507, "订单退款失败");
        }
        if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return Result.failure(507, "订单退款失败");
        }

        // 订单退款成功后的处理逻辑
        prepayCardOrder.setRefund(2);
        prepayCardOrder.setOrderStatus(5);
        int update = boxPrepayCardOrderService.updateWithOptimisticLocker(prepayCardOrder);
        if (update == 0) {
            // 如果更新失败，重新更新
            BoxPrepayCardOrder boxPrepayCardOrder = new BoxPrepayCardOrder();
            boxPrepayCardOrder.setId(prepayCardOrder.getId());
            boxPrepayCardOrder.setOrderStatus(5);
            boxPrepayCardOrder.setRefund(2);

            boxPrepayCardOrderService.updateById(boxPrepayCardOrder);
        }

        return Result.success();
    }
}
