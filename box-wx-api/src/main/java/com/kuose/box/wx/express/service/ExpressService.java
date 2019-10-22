package com.kuose.box.wx.express.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuose.box.common.utils.HttpUtil;
import com.kuose.box.wx.express.config.ExpressProperties;
import com.kuose.box.wx.express.entity.AppointmentExpressInfo;
import com.kuose.box.wx.express.entity.ExpressInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * 物流查询服务
 * <p>
 * 快递鸟即时查询API http://www.kdniao.com/api-track
 */
public class ExpressService {

    private final Log logger = LogFactory.getLog(ExpressService.class);
    /**
     * 快递鸟物流轨迹查询URL
     */
    private String ReqURL = "http://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";

    //测试请求url
    private String orderOnline = "http://testapi.kdniao.com:8081/api/oorderService";
    //正式请求url
//    private String orderOnline = "http://api.kdniao.com/api/OOrderService";



    private ExpressProperties properties;

    public ExpressProperties getProperties() {
        return properties;
    }

    public void setProperties(ExpressProperties properties) {
        this.properties = properties;
    }

    /**
     * 获取物流供应商名
     *
     * @param vendorCode
     * @return
     */
    public String getVendorName(String vendorCode) {
        for (Map<String, String> item : properties.getVendors()) {
            if (item.get("code").equals(vendorCode)) {
                return item.get("name");
            }
        }
        return null;
    }

    /**
     * 获取物流信息
     *
     * @param expCode  列子：YTO
     * @param expNo        YT4075859627914
     * @return
     */
    public ExpressInfo getExpressInfo(String expCode, String expNo) {
        try {
            String result = getOrderTracesByJson(expCode, expNo);
            ObjectMapper objMap = new ObjectMapper();
            ExpressInfo ei = objMap.readValue(result, ExpressInfo.class);
            ei.setShipperName(getVendorName(expCode));
            return ei;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }




    /**
     * Json方式 下线下单
     *
     * @throws Exception
     */
    public String orderOnlineByJson(AppointmentExpressInfo appointmentExpressInfo) throws Exception{
//        String requestData = JSONObject.toJSONString(appointmentExpressInfo);


        String requestData= "{'OrderCode': '0126577003123'," +
                "'ShipperCode':'SF'," +
                "'MonthCode':'7553045845'" +
                "'PayType':1," +
                "'ExpType':1," +
                "'Cost':1.0," +
                "'OtherCost':1.0," +
                "'Sender':" +
                "{" +
                "'Company':'LV','Name':'Taylor','Mobile':'15018442396','ProvinceName':'上海','CityName':'上海','ExpAreaName':'青浦区','Address':'明珠路73号'}," +
                "'Receiver':" +
                "{" +
                "'Company':'GCCUI','Name':'Yann','Mobile':'15018442396','ProvinceName':'北京','CityName':'北京','ExpAreaName':'朝阳区','Address':'三里屯街道雅秀大厦'}," +
                "'Commodity':" +
                "[{" +
                "'GoodsName':'鞋子','Goodsquantity':1,'GoodsWeight':1.0}]," +
                "'AddService':" +
                "[{" +
                "'Name':'COD','Value':'1020'}]," +
                "'Weight':1.0," +
                "'Quantity':1," +
                "'Volume':0.0," +
                "'Remark':'小心轻放'," +
                "'Commodity':" +
                "[{" +
                "'GoodsName':'鞋子'," +
                "'Goodsquantity':1," +
                "'GoodsWeight':1.0}]" +
                "}";

        Map<String, String> params = new HashMap<>(10);
        params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getAppId());
        params.put("RequestType", "1001");
        String dataSign=encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = HttpUtil.sendPost(orderOnline, params);

        //根据公司业务处理返回的信息......
        return result;
    }


    public String cancel() throws UnsupportedEncodingException {
                String requestData= "{'OrderCode': '1570790328964152'," +
                "'ShipperCode':'SF'," +
                "'ExpNo':'235681646491'}";


        Map<String, String> params = new HashMap<>(10);
        params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getAppId());
        params.put("RequestType", "1147");
        String dataSign=encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = HttpUtil.sendPost(orderOnline, params);

        //根据公司业务处理返回的信息......
        return result;
    }


    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    private String getOrderTracesByJson(String expCode, String expNo) throws Exception {
        if (!properties.isEnable()) {
            return null;
        }

        String requestData = "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";

        Map<String, String> params = new HashMap<>(10);
        params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        params.put("EBusinessID", properties.getAppId());
        params.put("RequestType", "1002");
        String dataSign = encrypt(requestData, properties.getAppKey(), "UTF-8");
        params.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = HttpUtil.sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......

        return result;
    }

    /**
     * MD5加密
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws Exception
     */
    private String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**
     * Sign签名生成
     *
     * @param content  内容
     * @param keyValue Appkey
     * @param charset  编码方式
     * @return DataSign签名
     */
    private String encrypt(String content, String keyValue, String charset) {
        if (keyValue != null) {
            content = content + keyValue;
        }
        byte[] src;
        try {
            src = MD5(content, charset).getBytes(charset);
            return Base64Utils.encodeToString(src);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }


}
