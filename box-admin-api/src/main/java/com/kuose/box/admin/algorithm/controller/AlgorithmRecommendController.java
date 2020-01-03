package com.kuose.box.admin.algorithm.controller;

/**
 * @author fangyajun
 * @description
 * @since 2019/11/21
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.algorithm.entity.RecommendGoods;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.goods.entity.BoxGoods;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 算法推荐，此控制器调用了算法api
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-23
 */
@Api(tags = {"算法推荐,算法api"})
@RestController
@RequestMapping("/boxGoodsAttributeLabel")
public class AlgorithmRecommendController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BoxGoodsService boxGoodsService;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;

    @ApiOperation(value="算法推荐，注意，返回结果分页信息total表示总的页数")
    @GetMapping("/algorithmRecommendGoods")
    public Result algorithmRecommendGoods(@RequestParam(defaultValue = "13018073079") String phone, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        String url = "http://192.168.5.194:9002/kuose_rec/userid/"+phone+"/page/"+ page +"/psize/"+ limit;
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);

        ResponseEntity<String> responseEntity  = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        String body = responseEntity.getBody();
        JSONObject jsonResult = JSONObject.parseObject(body);
        String message = jsonResult.getString("message");
        if (!"succuss".equals(message)) {
            return Result.failure("网络异常，暂无数据");
        }

        JSONArray skuArray = jsonResult.getJSONArray("res");
        if (skuArray == null || skuArray.size() == 0) {
            return Result.failure("暂无推荐数据");
        }

        IPage<RecommendGoods> goodsIPage = new Page<>();

        int curPage = jsonResult.getIntValue("pnum");
        int maxPage = jsonResult.getIntValue("max_page");
        List<String> skuList = skuArray.toJavaList(String.class);
        List<RecommendGoods> recommendGoodsList = listRecommendGoods(skuList);

        goodsIPage.setCurrent(curPage);
        goodsIPage.setSize(limit);
        goodsIPage.setTotal(maxPage);
        goodsIPage.setRecords(recommendGoodsList);
        return Result.success().setData("goodsIPage", goodsIPage);
    }

    private List<RecommendGoods> listRecommendGoods(List<String> skuList) {

        List<RecommendGoods> recommendGoodsList = new ArrayList<>();
        // 在添加
        for (String skuCode : skuList) {
            BoxGoodsSku goodsSku = boxGoodsSkuService.getOne(new QueryWrapper<BoxGoodsSku>().eq("sku_code", skuCode));
            if (goodsSku == null) {
               continue;
            }

            BoxGoods goods = boxGoodsService.getById(goodsSku.getGoodsId());
            if (goods == null) {
                continue;
            }
            RecommendGoods recommendGoods = new RecommendGoods();
            recommendGoods.setGoodsId(goods.getId());
            recommendGoods.setSkuId(goodsSku.getId());
            recommendGoods.setGoodsName(goods.getName());
            recommendGoods.setCategoryCode(goods.getCategoryCode());
            recommendGoods.setQuarter(goods.getQuarter());
            recommendGoods.setGoodsNo(goods.getGoodsNo());
            recommendGoods.setSkuNo(goodsSku.getSkuCode());
            recommendGoods.setPrice(goodsSku.getRetailPrice());
            recommendGoods.setColorName(goodsSku.getColorName());
            recommendGoods.setSizeName(goodsSku.getSizeName());
            recommendGoods.setPicUrl(goods.getOosImg());
            recommendGoods.setSkuUrl(goods.getOosImg());
            recommendGoods.setOldPicUrl(goods.getImg());
            recommendGoods.setTmallUrl(goods.getTmallUrl());

            recommendGoodsList.add(recommendGoods);
        }

        return recommendGoodsList;
    }
}
