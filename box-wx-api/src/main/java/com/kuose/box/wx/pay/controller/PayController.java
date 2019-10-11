package com.kuose.box.wx.pay.controller;

import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.user.entity.BoxUser;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.login.service.BoxUserService;
import com.kuose.box.wx.order.service.BoxOrderService;
import com.kuose.box.wx.pay.dto.PrePayVO;
import com.kuose.box.wx.pay.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/11
 */
@Api(tags = {"支付，微信支付"})
@RestController
public class PayController {
    @Autowired
    private PayService payService;
    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private BoxUserService boxUserService;




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
    @PostMapping("/prepay")
    public Result prepay(@RequestBody PrePayVO prePayVO, @ApiParam(hidden = true)  @LoginUser Integer userId, HttpServletRequest request) {
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

        if (!userId.equals(order.getUserId())) {
            return Result.failure(506, "当前登录的用户信息和订单的用户信息不一致");
        }

        // 验证订单是否能够付款
        // 订单状态,0-待搭配状态，1-已搭配待发货，2-已发货待收货，3-已确认收货待付款，4-，5-已支付待预约, 6-已预约待寄回, 7-寄回中 8-， 9-已完成，10-已关闭
        if (order.getOrderStatus() != 3) {
            return Result.failure(506, "该订单状态不是待付款状态，无法支付");
        }

        BoxUser user = boxUserService.getById(order.getUserId());
        if (StringUtil.isBlank(user.getWeixinOpenid())) {
            return Result.failure(506, "未能获取到用户openid，无法支付");
        }

        return payService.prepay(prePayVO.getOrderId(), user.getWeixinOpenid(),  request);

    }
}
