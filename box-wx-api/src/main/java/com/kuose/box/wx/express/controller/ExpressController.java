package com.kuose.box.wx.express.controller;

import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.common.utils.date.DateUtil;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.user.entity.BoxUserAddress;
import com.kuose.box.wx.express.dto.AppointmentExpressDTO;
import com.kuose.box.wx.express.dto.BackGoodsExpressDTO;
import com.kuose.box.wx.express.entity.*;
import com.kuose.box.wx.express.service.ExService;
import com.kuose.box.wx.express.service.ExpressService;
import com.kuose.box.wx.order.service.BoxOrderService;
import com.kuose.box.wx.user.service.BoxUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/10
 */
@Api(tags = {"物流信息"})
@RestController
public class ExpressController {

    @Autowired
    private ExpressService expressService;
    @Autowired
    private BoxOrderService boxOrderService;
    @Autowired
    private BoxUserAddressService boxUserAddressService;
    @Autowired
    private ExService exService;


    @ApiOperation(value="获取物流信息详情")
    @GetMapping("/getExpressDetail")
    public Result getExpressDetail(String expCode, String expNo) {
        ExpressInfo expressInfo = expressService.getExpressInfo(expCode, expNo);
        return Result.success().setData("expressInfo", expressInfo);
    }

    @ApiOperation(value="预约快递")
    @PostMapping("/appointmentExpress")
    public Result appointmentExpress(@RequestBody AppointmentExpressDTO appointmentExpressDTO) throws Exception {
        if (appointmentExpressDTO.getOrderId() == null || appointmentExpressDTO == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder order = boxOrderService.getById(appointmentExpressDTO.getOrderId());
        if (order == null) {
            return Result.failure("数据异常，查无此订单数据");
        }

        if (order.getOrderStatus() != 5) {
            return Result.failure("该订单不是待预约状态，请核对该订单的状态");
        }

        BoxUserAddress userAddress = boxUserAddressService.getById(appointmentExpressDTO.getAddrId());
        if (userAddress == null) {
            return Result.failure("数据异常，查无此地址信息");
        }

        AppointmentExpressInfo appointmentExpressInfo = new AppointmentExpressInfo();
        appointmentExpressInfo.setShipperCode("YTO");
        appointmentExpressInfo.setOrderCode(order.getOrderNo());
        appointmentExpressInfo.setIsNotice(0);
        appointmentExpressInfo.setPayType(2);
        appointmentExpressInfo.setExpType("1");

        Receiver receiver = new Receiver();
        receiver.setProvinceName("广东省");
        receiver.setCityName("广州市");
        receiver.setExpAreaName("海珠区");
        receiver.setAddress("UP智谷D栋2楼阔色公司");
        receiver.setMobile("15000273684");
        receiver.setName("方亚军");

        appointmentExpressInfo.setReceiver(receiver);

        Sender sender = new Sender();
        sender.setProvinceName(userAddress.getProvince());
        sender.setCityName(userAddress.getCity());
        sender.setExpAreaName(userAddress.getCounty());
        sender.setAddress(userAddress.getAddressDetail());
        sender.setMobile(userAddress.getPhone());
        sender.setName(userAddress.getName());

        appointmentExpressInfo.setSender(sender);


        List<Commodity> commodityList = new ArrayList<>();
        Commodity commodity = new Commodity();
        commodity.setGoodsName("衣服");
        commodity.setGoodsquantity(1);
        commodityList.add(commodity);

        appointmentExpressInfo.setCommodity(commodityList);

        Long startDate = appointmentExpressDTO.getStartDate();
        Long endDate = appointmentExpressDTO.getEndDate();
        DateUtil.timestampToStringTime(startDate);
        appointmentExpressInfo.setStartDate(DateUtil.timestampToStringTime(startDate));
        appointmentExpressInfo.setEndDate(DateUtil.timestampToStringTime(endDate));


//        appointmentExpressInfo.setStartDate("2019-10-29 13:00:00");
//        appointmentExpressInfo.setEndDate("2019-10-29 17:00:00");
        ////////////////////////正式上线了把此注释打开/////////////////////////////////
//
//        String json = expressService.orderOnlineByJson(appointmentExpressInfo);
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        boolean success = jsonObject.getBooleanValue("Success");
//        int resultCode = jsonObject.getIntValue("ResultCode");
//
//        if (resultCode == 106) {
//            return Result.failure("该订单已成功预约，请勿重复预约");
//        }
//
//        if (!success && resultCode != 100) {
//            return Result.failure("预约快递失败，请重新预约");
//        }
     //////////////////////////////////////////////////////////////////////
        order.setOrderStatus(6);
        boxOrderService.updateWithOptimisticLocker(order);
        return Result.success();
    }

    @ApiOperation(value="寄回商品物流信息填写")
    @PostMapping("/backGoodsExpress")
    public Result backGoodsExpress(@RequestBody BackGoodsExpressDTO backGoodsExpressDTO) {
        if (backGoodsExpressDTO.getOrderId() == null || StringUtil.isBlank(backGoodsExpressDTO.getBackShipSn()) ||
                StringUtil.isBlank(backGoodsExpressDTO.getBackShipChannel())) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder order = boxOrderService.getById(backGoodsExpressDTO.getOrderId());
        if (order == null) {
            return Result.failure("数据异常，查无此订单");
        }
        if (order.getOrderStatus() !=6 && order.getOrderStatus() != 7) {
            return Result.failure("订单状态不是预约状态，请核对订单状态");
        }

        order.setOrderStatus(7);
        order.setBackShipChannel(backGoodsExpressDTO.getBackShipChannel());
        order.setBackShipSn(backGoodsExpressDTO.getBackShipSn());

        int update = boxOrderService.updateWithOptimisticLocker(order);
        if (update == 0) {
            return Result.failure("更新数据异常");
        }

        return Result.success();
    }

    @GetMapping("/cancel")
    public Result cancel() throws UnsupportedEncodingException {
        String cancel = expressService.cancel();
        return Result.success();
    }

}
