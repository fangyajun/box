package com.kuose.box.admin.goods.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.goods.dto.AttributeSource;
import com.kuose.box.admin.goods.entity.BoxGoodsAttributeLabel;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeLabelService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.PinYinUtils;
import com.kuose.box.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * <p>
 * 商品属性标签表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-23
 */
@Api(tags = {"商品管理，商品属性标签管理"})
@RestController
@RequestMapping("/boxGoodsAttributeLabel")
public class BoxGoodsAttributeLabelController {

    @Autowired
    private BoxGoodsAttributeLabelService boxGoodsAttributeLabelService;
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value="添加商品属性标签")
    @PostMapping("/add")
    public Result add(@RequestBody BoxGoodsAttributeLabel boxGoodsAttributeLabel) {
        if (boxGoodsAttributeLabel.getParentId() == null || StringUtil.isBlank(boxGoodsAttributeLabel.getAttributeName())) {
            return Result.failure("缺少必传参数");
        }

        // 头节点的名称不能一样
        if (boxGoodsAttributeLabel.getParentId() == 0) {
            int count = boxGoodsAttributeLabelService.count(new QueryWrapper<BoxGoodsAttributeLabel>().eq("attribute_name", boxGoodsAttributeLabel.getAttributeName()).
                    eq("deleted", 0).eq("parent_id", 0));
            if (count >= 1) {
                return Result.failure("该参数名称已存在，请勿重复添加！");
            }
        }
        // 简码
        boxGoodsAttributeLabel.setAttributeCode(generateAttributeCode(boxGoodsAttributeLabel.getAttributeName()));
        boxGoodsAttributeLabel.setAddTime(System.currentTimeMillis());
        boxGoodsAttributeLabel.setUpdateTime(System.currentTimeMillis());

        boxGoodsAttributeLabelService.save(boxGoodsAttributeLabel);
        return Result.success();
    }

    private String generateAttributeCode(String attributeName) {
        // 生成简码
        String attributeCode = "";
        int count = 0;
        do {
            attributeCode = PinYinUtils.changeToGetShortPinYin(attributeName).toUpperCase() + PinYinUtils.getRandomString(3);
            count = boxGoodsAttributeLabelService.count(new QueryWrapper<BoxGoodsAttributeLabel>().eq("attribute_code", attributeCode).eq("deleted", 0));
        } while ( count >= 1);

        return attributeCode;
    }

    @ApiOperation(value="同步属性标签,从数据源同步属性")
    @GetMapping("/syncAttributeSource")
    public Result syncAttributeSource () {
        String listAllAttibutesUrl = "http://localhost:10303/attributeController/listAllAttibutes";
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);

        ResponseEntity<JSONObject> exchange = restTemplate.exchange(listAllAttibutesUrl, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject jsonResult = exchange.getBody();
        JSONObject data = jsonResult.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("attributeSourceList");
        if (jsonArray == null || jsonArray.size() == 0) {
            return Result.failure("无数据更新");
        }
        List<AttributeSource> attributeSourceList = jsonArray.toJavaList(AttributeSource.class);

        for (AttributeSource attributeSource : attributeSourceList) {
            String attributeGroupname = attributeSource.getAttributeGroupname();
            String attributeName = attributeSource.getAttributeName();
            String attributeData = attributeSource.getAttributeData();

            BoxGoodsAttributeLabel firstAttributeLabel = boxGoodsAttributeLabelService.getOne(new QueryWrapper<BoxGoodsAttributeLabel>().
                    eq("attribute_name", attributeGroupname).eq("deleted", 0).eq("parent_id", 0));
            // 没有查到，创建头节点
           if (firstAttributeLabel == null) {
               firstAttributeLabel = new BoxGoodsAttributeLabel();
               firstAttributeLabel.setParentId(0);
               firstAttributeLabel.setAttributeName(attributeGroupname);
               firstAttributeLabel.setAttributeCode(generateAttributeCode(attributeGroupname));
               firstAttributeLabel.setAttributeFlag("sync");
               firstAttributeLabel.setType(0);
               firstAttributeLabel.setUpdateTime(System.currentTimeMillis());
               firstAttributeLabel.setAddTime(System.currentTimeMillis());

               boxGoodsAttributeLabelService.save(firstAttributeLabel);
           }

           // 第二个节点
            BoxGoodsAttributeLabel secondAttributeLabel = boxGoodsAttributeLabelService.getOne(new QueryWrapper<BoxGoodsAttributeLabel>().
                    eq("parent_id", firstAttributeLabel.getId()).eq("deleted", 0).eq("attribute_name", attributeName));
            if (secondAttributeLabel == null) {
                secondAttributeLabel = new BoxGoodsAttributeLabel();
                secondAttributeLabel.setParentId(firstAttributeLabel.getId());
                secondAttributeLabel.setAttributeName(attributeName);
                secondAttributeLabel.setAttributeCode(generateAttributeCode(attributeName));
                secondAttributeLabel.setAttributeFlag("sync");
                secondAttributeLabel.setType(1);
                secondAttributeLabel.setUpdateTime(System.currentTimeMillis());
                secondAttributeLabel.setAddTime(System.currentTimeMillis());

               boxGoodsAttributeLabelService.save(secondAttributeLabel);
           }

           // 第三个节点（叶子节点）
            BoxGoodsAttributeLabel thirdAttributeLabel = boxGoodsAttributeLabelService.getOne(new QueryWrapper<BoxGoodsAttributeLabel>().
                    eq("parent_id", secondAttributeLabel.getId()).eq("deleted", 0).eq("attribute_name", attributeData));
            if (thirdAttributeLabel == null) {
                thirdAttributeLabel = new BoxGoodsAttributeLabel();
                thirdAttributeLabel.setParentId(secondAttributeLabel.getId());
                thirdAttributeLabel.setAttributeName(attributeData);
                thirdAttributeLabel.setAttributeCode(generateAttributeCode(attributeData));
                thirdAttributeLabel.setAttributeFlag("sync");
                thirdAttributeLabel.setType(1);
                thirdAttributeLabel.setUpdateTime(System.currentTimeMillis());
                thirdAttributeLabel.setAddTime(System.currentTimeMillis());

                boxGoodsAttributeLabelService.save(thirdAttributeLabel);
            }
        }

        return Result.success();
    }


    @ApiOperation(value="商品属性标签列表")
    @ApiImplicitParam(name = "attributeFlag", value = "属性属性标记,特殊时候需要填写，根据添加时候传入的", required = false, dataType = "String")
    @GetMapping("/list")
    public Result list(String attributeFlag) {
        List<BoxGoodsAttributeLabel> boxGoodsAttributeLabelList = boxGoodsAttributeLabelService.listGoodsAttributeLabel(attributeFlag);
        return Result.success().setData("boxGoodsAttributeLabelList", boxGoodsAttributeLabelList);
    }

    @ApiOperation(value="更新商品属性标签列表")
    @PostMapping("/update")
    public Result update(@RequestBody BoxGoodsAttributeLabel boxGoodsAttributeLabel) {
        if (boxGoodsAttributeLabel.getId() == null) {
            return Result.failure("缺少必传参数");
        }
        BoxGoodsAttributeLabel attributeLabel = boxGoodsAttributeLabelService.getById(boxGoodsAttributeLabel.getId());
        // 头节点的名称不能一样
        if (attributeLabel.getParentId() == 0) {
            int count = boxGoodsAttributeLabelService.count(new QueryWrapper<BoxGoodsAttributeLabel>().
                    eq("attribute_name", boxGoodsAttributeLabel.getAttributeName()).
                    eq("deleted", 0).ne("id",attributeLabel.getId()));
            if (count >= 1) {
                return Result.failure("该参数名称已存在，更新失败！");
            }
        }

        boxGoodsAttributeLabel.setUpdateTime(System.currentTimeMillis());
        boxGoodsAttributeLabelService.updateById(boxGoodsAttributeLabel);
        return Result.success();
    }

    @ApiOperation(value="删除商品属性标签列表")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxGoodsAttributeLabel boxGoodsAttributeLabel) {
        if (boxGoodsAttributeLabel.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxGoodsAttributeLabelService.removeById(boxGoodsAttributeLabel.getId());
        List<BoxGoodsAttributeLabel> boxGoodsAttributeLabels = boxGoodsAttributeLabelService.list(new QueryWrapper<BoxGoodsAttributeLabel>().eq("parent_id", boxGoodsAttributeLabel.getId()));
        remove(boxGoodsAttributeLabels);

        return Result.success();
    }

    /**
     * 递归删除节点
     * @param boxGoodsAttributeLabels
     */
    private void remove( List<BoxGoodsAttributeLabel> boxGoodsAttributeLabels) {
        if (boxGoodsAttributeLabels == null || boxGoodsAttributeLabels.size() < 1) {
            return;
        }

        for (BoxGoodsAttributeLabel boxGoodsAttributeLabel : boxGoodsAttributeLabels) {
            boxGoodsAttributeLabelService.removeById(boxGoodsAttributeLabel.getId());
            List<BoxGoodsAttributeLabel> attributeLabels = boxGoodsAttributeLabelService.list(new QueryWrapper<BoxGoodsAttributeLabel>().eq("parent_id", boxGoodsAttributeLabel.getId()));
            remove(attributeLabels);
        }
    }
}

