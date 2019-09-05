package com.kuose.box.admin.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.goods.entity.BoxGoodsSize;
import com.kuose.box.admin.goods.service.BoxGoodsSizeService;
import com.kuose.box.common.config.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 尺寸表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
@Api(tags = {"商品管理，商品尺寸"})
@RestController
@RequestMapping("/boxGoodsSize")
public class BoxGoodsSizeController {

    @Autowired
    private BoxGoodsSizeService boxGoodsSizeService;

    @ApiOperation(value="获取所有的尺寸")
    @GetMapping("/list")
    public Result list() {
        List<BoxGoodsSize> boxGoodsSizes = boxGoodsSizeService.list(new QueryWrapper<BoxGoodsSize>().eq("deleted", 0));
        if (boxGoodsSizes == null || boxGoodsSizes.size() == 0) {
            return Result.failure("暂无数据");
        }

        Map<String, String> goodsSize = new HashMap<>(20);
        for (BoxGoodsSize boxGoodsSize : boxGoodsSizes) {
            goodsSize.put(boxGoodsSize.getSizeCode(), boxGoodsSize.getSizeName());
        }

        return Result.success().setData("goodsSize",goodsSize);
    }

}

