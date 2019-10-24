package com.kuose.box.wx.pay.controller;

import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.prepay.entity.BoxPrepayCardOrder;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.login.service.BoxUserService;
import com.kuose.box.wx.order.service.BoxOrderService;
import com.kuose.box.wx.order.service.BoxPrepayCardOrderService;
import com.kuose.box.wx.pay.dto.PrePayVO;
import com.kuose.box.wx.pay.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;


/**
 * @author fangyajun
 * @description
 * @since 2019/10/11
 */
@Api(tags = {"支付，微信支付"})
@RequestMapping("/payController")
@RestController
public class PayController {


    @Autowired
    private PayService payService;
    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private BoxUserService boxUserService;
    @Autowired
    private BoxPrepayCardOrderService boxPrepayCardOrderService;



    @ApiOperation(value="判断订单是否还需支付金额，0：不需要支付，1：需要支付")
    @GetMapping("/isToPay")
    public Result isToPay(Integer orderId, @ApiParam(hidden = true)  @LoginUser Integer userId) {
        if (orderId == null) {
            return Result.failure("缺少必传参数");
        }
        BoxOrder order = boxOrderService.getById(orderId);
        if (order == null) {
            return Result.failure(506, "查无此订单，请确认参数是否正确");
        }

        if (order.getOrderStatus() != 4) {
            return Result.failure(506, "请先进行订单结算");
        }

        if (order.getActualPrice().compareTo(new BigDecimal("0")) == 1) {
            // 需要支付金额
            return Result.success().setData("isToPay", 1);
        } else {
            // 不需要支付金额
            if (order.getRefundPrepayAmounts().compareTo(new BigDecimal("0")) == 1) {
                // 预付金可退金额大于0，设置预付金订单可退金额
                BoxPrepayCardOrder boxPrepayCardOrder = new BoxPrepayCardOrder();
                boxPrepayCardOrder.setId(order.getPrepayCardOrderId());
                boxPrepayCardOrder.setRefundPrepayAmounts(order.getRefundPrepayAmounts());
                boxPrepayCardOrder.setUpdateTime(System.currentTimeMillis());
                boxPrepayCardOrderService.updateById(boxPrepayCardOrder);
            }

            order.setOrderStatus(5);
            int update = boxOrderService.updateWithOptimisticLocker(order);
            if (update == 0) {
                return Result.failure(507, "更新数据异常，请重试");
            }
            // 不需要支付金额
            return Result.success().setData("isToPay", 0);
        }
    }


    /**
     * 付款订单的预支付会话标识
     * <p>
     * 1. 检测当前订单是否能够付款
     * 2. 微信商户平台返回支付订单ID
     * 3. 设置订单付款状态
     *
     * @param userId 用户ID
     * @param prePayVO   订单信息，{ orderId：xxx }
     * @return 支付订单ID
     */
    @ApiOperation(value="付款订单的预支付会话标识")
    @PostMapping("/prepayOrder")
    public Result prepayOrder(@RequestBody PrePayVO prePayVO, @ApiParam(hidden = true)  @LoginUser Integer userId, HttpServletRequest request) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (prePayVO.getOrderId() == null) {
            return Result.failure(506, "参数不对");
        }

        BoxOrder order = boxOrderService.getById(prePayVO.getOrderId());
        if (order == null) {
            return Result.failure(506, "查无此订单，参数值错误");
        }

//        if (!userId.equals(order.getUserId())) {
//            return Result.failure(506, "当前登录的用户信息和订单的用户信息不一致");
//        }

        // 验证订单是否能够付款
        // 订单状态,0-待搭配状态，1-已搭配待发货，2-已发货待收货，3-已确认收货待付款，4-，5-已支付待预约, 6-已预约待寄回, 7-寄回中 8-， 9-已完成，10-已关闭
        if (order.getOrderStatus() != 3) {
            return Result.failure("该订单状态不是待付款状态，无法支付");
        }

        BoxUser user = boxUserService.getById(order.getUserId());
        if (StringUtil.isBlank(user.getWeixinOpenid())) {
            return Result.failure("未能获取到用户openid，无法支付");
        }

        return payService.prepayOrder(prePayVO.getOrderId(), user.getWeixinOpenid(),  request);
    }

    /**
     * 预付金订单的预支付会话标识
     * <p>
     * 1. 检测当前订单是否能够付款
     * 2. 微信商户平台返回支付订单ID
     * 3. 设置订单付款状态
     *
     * @param userId 用户ID
     * @param prePayVO   订单信息，{ orderId：xxx }
     * @return 支付订单ID
     */
    @ApiOperation(value="预付金订单的预支付会话标识")
    @PostMapping("/prepayCardOrder")
    public Result prepayCardOrder(@RequestBody PrePayVO prePayVO, @ApiParam(hidden = true)  @LoginUser Integer userId, HttpServletRequest request) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (prePayVO.getOrderId() == null) {
            return Result.failure(506, "参数不对");
        }

        BoxPrepayCardOrder prepayCardOrder = boxPrepayCardOrderService.getById(prePayVO.getOrderId());
        if (prepayCardOrder == null) {
            return Result.failure(506, "查无此订单，参数值错误");
        }

//        if (!userId.equals(prepayCardOrder.getUserId())) {
//            return Result.failure(506, "当前登录的用户信息和订单的用户信息不一致");
//        }

        // 验证订单是否能够付款
        // 订单状态,0-已提交未支付，1-已调用微信支付但未支付，2-已支付但未服务，3-服务中，4-，5-已完成，服务次数已用完，预付金已用完
        if (prepayCardOrder.getOrderStatus() != 0 && prepayCardOrder.getOrderStatus() != 1) {
            return Result.failure(506, "该订单状态不是待付款状态，无法支付");
        }

        BoxUser user = boxUserService.getById(prepayCardOrder.getUserId());
        if (StringUtil.isBlank(user.getWeixinOpenid())) {
            return Result.failure(506, "未能获取到用户openid，无法支付");
        }

        return payService.prepayCardOrder(prePayVO.getOrderId(), user.getWeixinOpenid(),  request);
    }


    /**
     * 微信付款成功或失败回调接口
     *
     * <p>
     * @param request 请求内容
     * @param response 响应内容
     * @return 操作结果
     */
    @PostMapping("payNotify")
    public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
        return payService.payNotify(request, response);
    }

    @ApiOperation(value="预付金退款,传入预付金订单id prepayCardOrderId ")
    @PostMapping("/refundPrePayCard")
    public Result refundPrePayCard(@RequestBody PrePayVO prePayVO, @ApiParam(hidden = true)  @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(506, "请登录");
        }

        return  payService.refundPrePayCard(prePayVO.getPrepayCardOrderId());
    }

}
