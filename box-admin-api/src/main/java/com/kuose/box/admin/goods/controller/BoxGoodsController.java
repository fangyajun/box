package com.kuose.box.admin.goods.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeLabelService;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeService;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.goods.dto.AttributeSource;
import com.kuose.box.db.goods.dto.GoodsAllinone;
import com.kuose.box.db.goods.dto.GoodsQueryParameter;
import com.kuose.box.db.goods.dto.GoodsSkuVo;
import com.kuose.box.db.goods.entity.BoxGoods;
import com.kuose.box.db.goods.entity.BoxGoodsAttribute;
import com.kuose.box.db.goods.entity.BoxGoodsAttributeLabel;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import io.swagger.annotations.Api;
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
 * 商品基本信息表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Api(tags = {"商品管理，商品管理"})
@RestController
@RequestMapping("/boxGoods")
public class BoxGoodsController {

    @Autowired
    private BoxGoodsService boxGoodsService;
    @Autowired
    private BoxGoodsAttributeService boxGoodsAttributeService;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BoxGoodsAttributeLabelService boxGoodsAttributeLabelService;

    @ApiOperation(value="添加商品")
    @PostMapping("/add")
    public Result add(@RequestBody GoodsAllinone goodsAllinone) {
        if (goodsAllinone.getBoxGoods() == null || goodsAllinone.getBoxGoodsAttributes() == null || goodsAllinone.getBoxGoodsSkus() == null) {
            return Result.failure("缺少参数");
        }
        if (StringUtil.isBlank(goodsAllinone.getBoxGoods().getName())) {
            return Result.failure("缺少商品基本数据");
        }

        Result result = boxGoodsService.save(goodsAllinone);
        return result;
    }

    @ApiOperation(value="同步商品，从数据源同步商品,只更新添加的数据，不更新修改的数据")
    @GetMapping("/syncGoods")
    public Result syncGoods(String year) {
        // 从数据源获取所有商品
//        String listGoodsAttibutesUrl = "http://localhost:10303/ripreportProductinformation/listGoods?year=" + year;
        String listGoodsAttibutesUrl = "http://192.168.5.177:10303/ripreportProductinformation/listGoods?year=" + year;
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);

        ResponseEntity<JSONObject> exchange = restTemplate.exchange(listGoodsAttibutesUrl, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject jsonResult = exchange.getBody();
        JSONObject data = jsonResult.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("goodsList");
        if (jsonArray == null || jsonArray.size() == 0) {
            return Result.failure("商品数据源暂无数据");
        }
        List<BoxGoods> boxGoodsList = jsonArray.toJavaList(BoxGoods.class);
        int i = 1;
        for (BoxGoods boxGoods : boxGoodsList) {
            System.out.println("-------------------同步商品数据" + i++);
            // 查询此商品是否已经添加
            int count = boxGoodsService.count(new QueryWrapper<BoxGoods>().eq("deleted", 0).eq("goods_no", boxGoods.getGoodsNo()));
            if (count > 0) {
                continue;
            }
            // 获取商品的sku 信息
            List<BoxGoodsSku> goodsSku = getGoodsSku(boxGoods.getGoodsNo());
            // 程序跑到这，表示需要添加
            GoodsAllinone goodsAllinone = new GoodsAllinone();
            goodsAllinone.setBoxGoods(boxGoods);
            if (goodsSku != null && goodsSku.size() >0) {
                BoxGoodsSku[] boxGoodsSkus = goodsSku.toArray(new BoxGoodsSku[goodsSku.size()]);
                goodsAllinone.setBoxGoodsSkus(boxGoodsSkus);
            }

            boxGoodsService.save(goodsAllinone);
        }

        return Result.success();
    }

    private List<BoxGoodsSku> getGoodsSku(String productno) {
//        String listGoodsAttibutesUrl = "http://localhost:10303/ripreportProductinformation/getGoods?productno=" + productno;
        String listGoodsAttibutesUrl = "http://192.168.5.177:10303/ripreportProductinformation/getGoods?productno=" + productno;
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);

        ResponseEntity<JSONObject> exchange = restTemplate.exchange(listGoodsAttibutesUrl, HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject jsonResult = exchange.getBody();
        JSONObject data = jsonResult.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("goodsSkuList");
        if (jsonArray == null || jsonArray.size() == 0) {
            return null;
        }
        List<BoxGoodsSku> boxGoodsSkus = jsonArray.toJavaList(BoxGoodsSku.class);
        return boxGoodsSkus;
    }



    @ApiOperation(value="删除商品")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxGoods boxGoods) {
        if (boxGoods.getId() == null) {
            return Result.failure("缺少参数");
        }

        boxGoodsService.deleteGoods(boxGoods);
        return Result.success();
    }

    @ApiOperation(value="同步商品属性标签,从数据源同步商品属性")
    @GetMapping("/syncGoodsAttribute")
    public Result syncGoodsAttribute() {
        // 获取所有商品数据
        List<BoxGoods> goodsList = boxGoodsService.list(new QueryWrapper<BoxGoods>().eq("deleted", 0));
        int i = 1;
        for (BoxGoods goods : goodsList) {
            if (goods.getSourceGoodsId() == null) {
                continue;
            }
            System.out.println("-------------------同步商品属性数据" + i++);
//             String listGoodsAttibutesUrl = "http://localhost:10303/attributeController/getGoodsAttibutes?id=" + goods.getSourceGoodsId();
            String listGoodsAttibutesUrl = "http://192.168.5.177:10303/ripreportProductinformation/getGoodsAttibutes?id=" + goods.getSourceGoodsId();
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<>(null, requestHeaders);

            ResponseEntity<JSONObject> exchange = restTemplate.exchange(listGoodsAttibutesUrl, HttpMethod.GET, httpEntity, JSONObject.class);
            JSONObject jsonResult = exchange.getBody();
            JSONObject data = jsonResult.getJSONObject("data");
            JSONArray jsonArray = data.getJSONArray("goodsAttributeList");
            if (jsonArray == null || jsonArray.size() == 0) {
                continue;
            }

            List<AttributeSource> attributeSourceList = jsonArray.toJavaList(AttributeSource.class);
            for (AttributeSource attributeSource : attributeSourceList) {

                String attributeGroupname = attributeSource.getAttributeGroupName();
                String attributeGroupType = attributeSource.getAttributeGroupType();
                String attributeName = attributeSource.getAttributeName();
                String attributeData = attributeSource.getAttributeValueName();

                // 查询商品属性对应的属性编码
                QueryWrapper<BoxGoodsAttributeLabel> queryFirstAttribute = new QueryWrapper<BoxGoodsAttributeLabel>().eq("parent_id", 0).
                        eq("attribute_name", attributeGroupname).eq("head_node_category", attributeGroupType).eq("deleted", 0).eq("attribute_flag", "sync");
                BoxGoodsAttributeLabel firstAttribute = boxGoodsAttributeLabelService.getOne(queryFirstAttribute);
                if (firstAttribute == null) {
                    continue;
                }
                QueryWrapper<BoxGoodsAttributeLabel> querySecondAttribute = new QueryWrapper<BoxGoodsAttributeLabel>().eq("parent_id", firstAttribute.getId()).
                        eq("attribute_name", attributeName).eq("deleted", 0).eq("attribute_flag", "sync");
                BoxGoodsAttributeLabel secondAttribute = boxGoodsAttributeLabelService.getOne(querySecondAttribute);
                if (secondAttribute == null) {
                    continue;
                }
                QueryWrapper<BoxGoodsAttributeLabel> queryThirdAttribute = new QueryWrapper<BoxGoodsAttributeLabel>().eq("parent_id", secondAttribute.getId()).
                        eq("attribute_name", attributeData).eq("deleted", 0).eq("attribute_flag", "sync");
                BoxGoodsAttributeLabel thirdAttribute = boxGoodsAttributeLabelService.getOne(queryThirdAttribute);
                if (thirdAttribute == null) {
                    continue;
                }

                // 判断属性在商品属性表中是否存在
                int count = boxGoodsAttributeService.count(new QueryWrapper<BoxGoodsAttribute>().eq("deleted", 0).eq("goods_id", goods.getId()).
                        eq("attribute_code", thirdAttribute.getAttributeCode()));
                if (count >= 1) {
                    continue;
                }

                // 添加属性
                BoxGoodsAttribute goodsAttribute = new BoxGoodsAttribute();
                goodsAttribute.setGoodsId(goods.getId());
                goodsAttribute.setAttribute(thirdAttribute.getAttributeName());
                goodsAttribute.setAttributeCode(thirdAttribute.getAttributeCode());
                goodsAttribute.setAddTime(System.currentTimeMillis());
                goodsAttribute.setUpdateTime(System.currentTimeMillis());

                boxGoodsAttributeService.save(goodsAttribute);
            }
        }

        return Result.success();
    }

    @ApiOperation(value="商品列表")
    @GetMapping("list")
    public Result list(GoodsQueryParameter goodsQueryParameter, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Page<BoxGoods> boxGoodsPage = new Page<>();
        boxGoodsPage.setSize(limit);
        boxGoodsPage.setCurrent(page);

        IPage<BoxGoods> boxGoodsIPage = boxGoodsService.listGoodsPage(boxGoodsPage, goodsQueryParameter);
        return Result.success().setData("boxGoodsIPage", boxGoodsIPage);
    }

    @ApiOperation(value="商品详情")
    @GetMapping("/detail")
    public Result detail(Integer id) {
        if (id == null) {
            return Result.failure("参数必传");
        }
        BoxGoods boxGoods = boxGoodsService.getOne(new QueryWrapper<BoxGoods>().eq("id", id).eq("deleted", 0));
        List<BoxGoodsAttribute> goodsAttributeList = boxGoodsAttributeService.list(new QueryWrapper<BoxGoodsAttribute>().eq("goods_id", id).eq("deleted", 0));
        if (goodsAttributeList != null && goodsAttributeList.size() >= 0) {
            for (BoxGoodsAttribute boxGoodsAttribute : goodsAttributeList) {
                String attributeCode = boxGoodsAttribute.getAttributeCode();
                BoxGoodsAttributeLabel thirdGoodsAttributeLabel = boxGoodsAttributeLabelService.getOne(new QueryWrapper<BoxGoodsAttributeLabel>().eq("attribute_code", attributeCode));
                BoxGoodsAttributeLabel secondNode = boxGoodsAttributeLabelService.getById(thirdGoodsAttributeLabel.getParentId());
                if (secondNode != null) {
                    BoxGoodsAttribute attribute = new BoxGoodsAttribute();
                    attribute.setAttributeCode(secondNode.getAttributeCode());
                    attribute.setAttribute(secondNode.getAttributeName());

                    BoxGoodsAttributeLabel  firstNode = boxGoodsAttributeLabelService.getById(secondNode.getParentId());
                    if (firstNode != null) {
                        BoxGoodsAttribute firstAttribute = new BoxGoodsAttribute();
                        firstAttribute.setAttributeCode(firstNode.getAttributeCode());
                        firstAttribute.setAttribute(firstNode.getAttributeName());

                        attribute.setParentNode(firstAttribute);
                    }

                    boxGoodsAttribute.setParentNode(attribute);
                }
            }

        }

        List<BoxGoodsSku> goodsSkuList = boxGoodsSkuService.list(new QueryWrapper<BoxGoodsSku>().eq("goods_id", id).eq("deleted", 0));
        return Result.success().setData("boxGoods", boxGoods).setData("goodsAttributeList", goodsAttributeList).setData("goodsSkuList", goodsSkuList);
    }

    @ApiOperation(value="修改商品")
    @PostMapping("update")
    public Result update(@RequestBody GoodsAllinone goodsAllinone) {
        if (goodsAllinone.getBoxGoods() == null || goodsAllinone.getBoxGoodsAttributes() == null || goodsAllinone.getBoxGoodsSkus() == null) {
            return Result.failure("缺少参数");
        }
        if (StringUtil.isBlank(goodsAllinone.getBoxGoods().getName())) {
            return Result.failure("商品名称不能为空");
        }

        Result result = boxGoodsService.update(goodsAllinone);
        return result;
    }



    @ApiOperation(value="商品列表（包含SKU信息）")
    @GetMapping("/listGoodsAndSku")
    public Result listGoodsAndSku(GoodsQueryParameter goodsQueryParameter, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Page<BoxGoods> boxGoodsPage = new Page<>();
        boxGoodsPage.setSize(limit);
        boxGoodsPage.setCurrent(page);

        IPage<GoodsSkuVo> boxGoodsIPage = boxGoodsService.listGoodsAndSku(boxGoodsPage, goodsQueryParameter);
        return Result.success().setData("boxGoodsIPage", boxGoodsIPage);
    }

}

