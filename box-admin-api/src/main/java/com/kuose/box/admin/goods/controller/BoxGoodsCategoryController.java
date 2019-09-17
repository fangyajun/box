package com.kuose.box.admin.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.goods.entity.BoxGoodsCategory;
import com.kuose.box.admin.goods.service.BoxGoodsCategoryService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
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

//    @ApiOperation(value="添加商品分类")
//    @PostMapping("/add")
//    public Result add(@RequestBody BoxGoodsCategory goodsCategory) {
//        if (StringUtil.isBlank(goodsCategory.getCategoryName()) || StringUtil.isBlank(goodsCategory.getCategoryCode())) {
//            return Result.failure("缺少必传参数");
//        }
//
//        int count = boxGoodsCategoryService.count(new QueryWrapper<BoxGoodsCategory>().eq("deleted", 0).eq("category_name", goodsCategory.getCategoryName()).
//                or().eq("category_code", goodsCategory.getCategoryCode()));
//        if (count >= 1){
//            return Result.failure("分类名称或者分类编码已经存在");
//        }
//
//        goodsCategory.setPinyin(PinYinUtils.changeToTonePinYin(goodsCategory.getCategoryName()));
//        goodsCategory.setAddTime(System.currentTimeMillis());
//        goodsCategory.setUpdateTime(System.currentTimeMillis());
//        boxGoodsCategoryService.save(goodsCategory);
//        return Result.success();
//    }
//
//    @ApiOperation(value="更新商品分类")
//    @PostMapping("/update")
//    public Result update(@RequestBody BoxGoodsCategory goodsCategory) {
//        if (goodsCategory.getId() == null) {
//            return Result.failure("缺少必传参数");
//        }
//
//        int count = boxGoodsCategoryService.count(new QueryWrapper<BoxGoodsCategory>().eq("deleted", 0).ne("id",goodsCategory.getId()).
//                eq("category_name", goodsCategory.getCategoryName()).or().eq("category_code", goodsCategory.getCategoryCode()));
//        if (count >= 1){
//            return Result.failure("分类名称或者分类编码已经存在");
//        }
//
//        goodsCategory.setUpdateTime(System.currentTimeMillis());
//        boxGoodsCategoryService.updateById(goodsCategory);
//        return Result.success();
//    }

//    @ApiOperation(value="获取商品分类列表")
//    @GetMapping("/list")
//    public Result list(String categoryName) {
//        QueryWrapper<BoxGoodsCategory> queryWrapper = new QueryWrapper<BoxGoodsCategory>().eq("parent_id", 0).eq("deleted", 0);
//        if (!StringUtil.isBlank(categoryName)) {
//            queryWrapper.like("category_name", categoryName).or().like("pinyin", categoryName);
//        }
//
//        List<BoxGoodsCategory> goodsCategoryList = boxGoodsCategoryService.list(queryWrapper);
//        return Result.success().setData("goodsCategoryList", goodsCategoryList);
//    }



    @ApiOperation(value="获取头分类列表")
    @GetMapping("/list")
    public Result listHeadCategory(String categoryName) {
        QueryWrapper<BoxGoodsCategory> queryWrapper = new QueryWrapper<BoxGoodsCategory>().eq("parent_id", 0).eq("deleted", 0);
        if (!StringUtil.isBlank(categoryName)) {
            queryWrapper.like("category_name", categoryName).or().like("pinyin", categoryName);
        }

        List<BoxGoodsCategory> goodsCategoryList = boxGoodsCategoryService.list(queryWrapper);
        if (goodsCategoryList == null || goodsCategoryList.size() == 0) {
            return Result.failure("暂无数据");
        }

        Map<String, String> category = new HashMap<>();
        for (BoxGoodsCategory boxGoodsCategory : goodsCategoryList) {
            category.put(boxGoodsCategory.getCategoryCode(), boxGoodsCategory.getCategoryName());
        }
        return Result.success().setData("category", category);
    }

    @ApiOperation(value="获取子分类列表")
    @GetMapping("/listChildCategory")
    public Result listChildCategory(Integer id,  String categoryName) {
        if (id == null) {
            return Result.failure("缺少必传参数");
        }

        QueryWrapper<BoxGoodsCategory> queryWrapper = new QueryWrapper<BoxGoodsCategory>().eq("parent_id", id).eq("deleted", 0);
        if (!StringUtil.isBlank(categoryName)) {
            queryWrapper.like("category_name", categoryName).or().like("pinyin", categoryName);
        }

        List<BoxGoodsCategory> goodsCategoryList = boxGoodsCategoryService.list(queryWrapper);
        if (goodsCategoryList == null || goodsCategoryList.size() == 0) {
            return Result.failure("暂无数据");
        }

        Map<String, String> category = new HashMap<>();
        for (BoxGoodsCategory boxGoodsCategory : goodsCategoryList) {
            category.put(boxGoodsCategory.getCategoryCode(), boxGoodsCategory.getCategoryName());
        }
        return Result.success().setData("category", category);
    }


//    @ApiOperation(value="删除商品分类列表")
//    @PostMapping("/delete")
//    public Result delete(@RequestBody BoxGoodsCategory goodsCategory) {
//        if (goodsCategory.getId() == null) {
//            return Result.failure("缺少必传参数");
//        }
//
//        goodsCategory.setDeleted(1);
//        goodsCategory.setUpdateTime(System.currentTimeMillis());
//        boxGoodsCategoryService.updateById(goodsCategory);
//        return Result.success();
//    }
}

