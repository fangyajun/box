package com.kuose.box.admin.match.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.match.dto.GoodsMatchParameter;
import com.kuose.box.admin.match.service.GoodsMatchService;
import com.kuose.box.admin.user.service.BoxUserBaseService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.goods.dto.GoodsQueryParameter;
import com.kuose.box.db.goods.dto.GoodsSkuVo;
import com.kuose.box.db.goods.entity.BoxGoods;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import com.kuose.box.db.user.entity.BoxUserBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/9
 */
@Api(tags = {"搭配管理，商品推荐"})
@RestController
@RequestMapping("/goodsMatch")
public class GoodsMatchController {
    @Autowired
    private BoxUserBaseService boxUserBaseService;
    @Autowired
    private GoodsMatchService goodsMatchService;
    @Autowired
    private BoxGoodsService boxGoodsService;

    @ApiOperation(value="商品列表（匹配）")
    @GetMapping("/listRecommendGoods")
    public Result listRecommendGoods(GoodsMatchParameter goodsMatchParameter, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        if (goodsMatchParameter.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }
        BoxUserBase userBase = boxUserBaseService.getOne(new QueryWrapper<BoxUserBase>().eq("user_id", goodsMatchParameter.getUserId()).eq("deleted", 0));
        if (userBase == null) {
            return Result.failure("数据异常,无此用户的基本信息");
        }

        Page<BoxGoods> boxGoodsPage = new Page<>();
        boxGoodsPage.setSize(limit);
        boxGoodsPage.setCurrent(page);
        IPage<GoodsSkuVo> boxGoodsIPage = goodsMatchService.listMatchGoods(boxGoodsPage, goodsMatchParameter, userBase);
        return Result.success().setData("boxGoodsIPage", boxGoodsIPage);
    }


    @ApiOperation(value="商品列表（包含SKU信息）")
    @GetMapping("/listGoodsAndSku")
    public Result listGoodsAndSku(GoodsQueryParameter goodsQueryParameter, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, Integer userId) {
        Page<BoxGoods> boxGoodsPage = new Page<>();
        boxGoodsPage.setSize(limit);
        boxGoodsPage.setCurrent(page);

        IPage<GoodsSkuVo> boxGoodsIPage = boxGoodsService.listGoodsAndSku(boxGoodsPage, goodsQueryParameter);
        List<GoodsSkuVo> records = boxGoodsIPage.getRecords();
        // 过滤标记商品
        if (records != null && records.size() >= 1) {
            List<GoodsSkuVo> goodsFilter = goodsFilter(records, userId);
            boxGoodsIPage.setRecords(goodsFilter);
        }

        return Result.success().setData("boxGoodsIPage", boxGoodsIPage);
    }
    /**
     * 过滤掉没有sku的商品
      */
    private List<GoodsSkuVo>  goodsFilter(List<GoodsSkuVo> goodsSkuVoList, Integer userId) {

       if (goodsSkuVoList == null || goodsSkuVoList.isEmpty()) {
           return goodsSkuVoList;
       }

        Iterator<GoodsSkuVo> goodsSkuVoIterator = goodsSkuVoList.iterator();
       while (goodsSkuVoIterator.hasNext()) {
           Map<String,Object> map = (Map<String, Object>) goodsSkuVoIterator.next();
           List<BoxGoodsSku> boxGoodsSkuList = (List<BoxGoodsSku>) map.get("boxGoodsSkuList");

           if (boxGoodsSkuList == null || boxGoodsSkuList.isEmpty()) {
               goodsSkuVoIterator.remove();
           }
       }

        return goodsSkuVoList;
    }

}
