package com.kuose.source.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.source.goods.entity.BoxGoods;
import com.kuose.source.goods.entity.BoxGoodsSku;
import com.kuose.source.goods.entity.RipreportProductinformation;
import com.kuose.source.goods.service.RipreportProductinformationService;
import com.kuose.source.goods.service.RipreportProductinformationskuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *     SCM数据库的ripreportProductinformation表的
 *     前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
@RestController
@RequestMapping("/ripreportProductinformation")
public class RipreportProductInformationController {

    @Autowired
    private RipreportProductinformationService ripreportProductinformationService;
    @Autowired
    private RipreportProductinformationskuService ripreportProductinformationskuService;

    @GetMapping("/list")
    public Result list(String productno, String goodsName) {
        QueryWrapper<RipreportProductinformation> ripreportProductinformationQueryWrapper = new QueryWrapper<>();

        if (!StringUtil.isBlank(productno)) {
            ripreportProductinformationQueryWrapper.like("prodectno", productno);
        }
        if (!StringUtil.isBlank(goodsName)) {
            ripreportProductinformationQueryWrapper.like("thirdCategoryStr", goodsName);
        }
        List<RipreportProductinformation> goodsList = ripreportProductinformationService.list(ripreportProductinformationQueryWrapper);
        return Result.success().setData("goodsList", goodsList);
    }

    @GetMapping("/listGoods")
    public Result listGoods(String productno, String goodsName) {
        List<BoxGoods> goodsList = ripreportProductinformationService.listGoods(productno, goodsName);
        return Result.success().setData("goodsList", goodsList);
    }

    @GetMapping("/getGoods")
    public Result getGoods(String productno) {
        if (StringUtil.isBlank(productno)) {
            return Result.failure("参数商品货号必传");
        }
        BoxGoods boxGoods = ripreportProductinformationService.getGoods(productno);
        List<BoxGoodsSku> goodsSkuList = ripreportProductinformationskuService.listGoodsSku(boxGoods.getGoodsNo());
        return Result.success().setData("boxGoods", boxGoods).setData("goodsSkuList", goodsSkuList);
    }

}

