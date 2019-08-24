package com.kuose.box.admin.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.goods.entity.BoxGoodsCategory;
import com.kuose.box.admin.goods.service.BoxGoodsCategoryService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.PinYinUtils;
import com.kuose.box.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Api(tags = {"商品管理，商品类别管理"})
@RestController
@RequestMapping("/boxGoodsCategory")
public class BoxGoodsCategoryController {

    @Autowired
    private BoxGoodsCategoryService boxGoodsCategoryService;

    @ApiOperation(value="添加商品分类")
    @PostMapping("/add")
    public Result add(@RequestBody BoxGoodsCategory goodsCategory) {
        if (StringUtil.isBlank(goodsCategory.getCategoryName()) || StringUtil.isBlank(goodsCategory.getCategoryCode())) {
            return Result.failure("缺少必传参数");
        }

        goodsCategory.setPinyin(PinYinUtils.changeToTonePinYin(goodsCategory.getCategoryName()));
        goodsCategory.setAddTime(System.currentTimeMillis());
        goodsCategory.setUpdateTime(System.currentTimeMillis());
        boxGoodsCategoryService.save(goodsCategory);
        return Result.success();
    }

    @ApiOperation(value="更新商品分类")
    @PostMapping("/update")
    public Result update(@RequestBody BoxGoodsCategory goodsCategory) {
        if (goodsCategory.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        goodsCategory.setUpdateTime(System.currentTimeMillis());
        boxGoodsCategoryService.updateById(goodsCategory);
        return Result.success();
    }

    @ApiOperation(value="获取商品分类列表")
    @GetMapping("/list")
    public Result list(String categoryName) {
        QueryWrapper<BoxGoodsCategory> queryWrapper = new QueryWrapper<BoxGoodsCategory>().eq("parent_id", 0).eq("deleted", 0);
        if (!StringUtil.isBlank(categoryName)) {
            queryWrapper.like("category_name", categoryName).or().like("pinyin", categoryName);
        }

        List<BoxGoodsCategory> goodsCategoryList = boxGoodsCategoryService.list(queryWrapper);
        return Result.success().setData("goodsCategoryList", goodsCategoryList);
    }

    @ApiOperation(value="删除商品分类列表")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxGoodsCategory goodsCategory) {
        if (goodsCategory.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        goodsCategory.setDeleted(1);
        goodsCategory.setUpdateTime(System.currentTimeMillis());
        boxGoodsCategoryService.updateById(goodsCategory);
        return Result.success();
    }
}

