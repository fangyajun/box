package com.kuose.box.admin.express.service;

import com.alibaba.fastjson.JSONObject;
import com.kuose.box.admin.express.entity.ExpressInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/17
 */
@Service
public class ExpressService {

    @Autowired
    private RestTemplate restTemplate;


    public ExpressInfo  getExpressDetail(String expCode, String expNo) {

//        String listGoodsAttibutesUrl = "http://192.168.5.177:10303/ripreportProductinformation/getGoodsAttibutes?expCode=" + expCode + "&expNo=" + expNo;
        String listGoodsAttibutesUrl = "http://localhost:10301/getExpressDetail?expCode=" + expCode + "&expNo=" + expNo;
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);

        ResponseEntity<JSONObject> exchange = restTemplate.exchange(listGoodsAttibutesUrl, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject jsonResult = exchange.getBody();
        JSONObject data = jsonResult.getJSONObject("data");
        JSONObject jsonObject = data.getJSONObject("expressInfo");
        if (jsonObject == null ) {
            return null;
        }

        ExpressInfo expressInfo = jsonObject.toJavaObject(ExpressInfo.class);

        return expressInfo;
    }



}
