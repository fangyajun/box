package com.kuose.box.wx.express.controller;

import com.kuose.box.common.config.Result;
import com.kuose.box.db.order.entity.BoxOrder;
import com.kuose.box.db.user.entity.BoxUserAddress;
import com.kuose.box.wx.express.dto.appointmentExpressDTO;
import com.kuose.box.wx.express.entity.*;
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


    @ApiOperation(value="获取物流信息详情")
    @GetMapping("/getExpressDetail")
    public Result getExpressDetail(String expCode, String expNo) {
        ExpressInfo expressInfo = expressService.getExpressInfo(expCode, expNo);
        return Result.success().setData("expressInfo", expressInfo);
    }

    @ApiOperation(value="预约快递")
    @PostMapping("/getExpressDetail")
    public Result appointmentExpress(@RequestBody appointmentExpressDTO appointmentExpressDTO) throws Exception {
        if (appointmentExpressDTO.getOrderId() == null || appointmentExpressDTO == null) {
            return Result.failure("缺少必传参数");
        }

        BoxOrder order = boxOrderService.getById(appointmentExpressDTO.getOrderId());
        if (order == null) {
            return Result.failure("数据异常，查无此订单数据");
        }

        BoxUserAddress userAddress = boxUserAddressService.getById(appointmentExpressDTO.getAddrId());
        if (userAddress == null) {
            return Result.failure("数据异常，查无此地址信息");
        }

        AppointmentExpressInfo appointmentExpressInfo = new AppointmentExpressInfo();
        appointmentExpressInfo.setShipperCode("YTO");
        appointmentExpressInfo.setOrderCode(order.getOrderNo());
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
        appointmentExpressInfo.setStartDate("2019-10-16 13:00:00");
        appointmentExpressDTO.setEndDate("2019-10-16 17:00:00");

        String json = expressService.orderOnlineByJson(appointmentExpressInfo);

        return Result.success().setData("json", json);
    }

    @GetMapping("/cancel")
    public Result cancel() throws UnsupportedEncodingException {
        String cancel = expressService.cancel();
        return Result.success();
    }

}
