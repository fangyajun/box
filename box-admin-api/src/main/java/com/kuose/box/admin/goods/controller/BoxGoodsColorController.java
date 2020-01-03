package com.kuose.box.admin.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.goods.service.BoxGoodsColorService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.goods.entity.BoxGoodsColor;
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
 * 颜色表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-27
 */
@Api(tags = {"商品管理，颜色管理"})
@RestController
@RequestMapping("/boxGoodsColor")
public class BoxGoodsColorController {

    @Autowired
    private BoxGoodsColorService boxGoodsColorService;

//    @ApiOperation(value="添加颜色")
//    @PostMapping("/add")
//    public Result add(@RequestBody BoxGoodsColor boxGoodsColor) {
//        if (StringUtil.isBlank(boxGoodsColor.getColorName()) || StringUtil.isBlank(boxGoodsColor.getColorCode())) {
//            return Result.failure("缺少必传参数");
//        }
//
//        if (boxGoodsColorService.count(new QueryWrapper<BoxGoodsColor>().eq("deleted", 0).eq("color_code", boxGoodsColor.getColorCode()).or().
//                eq("color_name", boxGoodsColor.getColorName())) >= 1) {
//            return Result.failure("颜色名称或者颜色编码以及存在");
//        }
//
//        boxGoodsColor.setAddTime(System.currentTimeMillis());
//        boxGoodsColor.setUpdateTime(System.currentTimeMillis());
//        boxGoodsColorService.save(boxGoodsColor);
//        return Result.success();
//    }
//
//    @ApiOperation(value="删除颜色")
//    @PostMapping("/delete")
//    public Result delete(@RequestBody BoxGoodsColor boxGoodsColor) {
//        if (boxGoodsColor.getId() == null) {
//            return Result.failure("缺少必传参数");
//        }
//
//        // 逻辑删除
//        boxGoodsColor.setDeleted(1);
//        boxGoodsColorService.updateById(boxGoodsColor);
//        return Result.success();
//    }
//
//    @ApiOperation(value="修改颜色")
//    @PostMapping("/update")
//    public Result update(@RequestBody BoxGoodsColor boxGoodsColor) {
//        if (boxGoodsColor.getId() == null) {
//            return Result.failure("缺少必传参数");
//        }
//
//        int count = boxGoodsColorService.count(new QueryWrapper<BoxGoodsColor>().eq("deleted", 0).ne("id", boxGoodsColor.getId()).
//                eq("color_code", boxGoodsColor.getColorCode()).or().eq("color_name", boxGoodsColor.getColorName()));
//        if (count >=1) {
//            return Result.failure("颜色名称或者颜色编码以及存在");
//        }
//
//        boxGoodsColor.setUpdateTime(System.currentTimeMillis());
//        boxGoodsColorService.updateById(boxGoodsColor);
//        return Result.success();
//    }

    @ApiOperation(value="颜色列表")
    @GetMapping("/list")
    public Result list(String queryCondition) {
        QueryWrapper<BoxGoodsColor> queryWrapper = new QueryWrapper<BoxGoodsColor>().eq("color_status", 0).eq("deleted", 0);
        if (!StringUtil.isBlank(queryCondition)) {
            queryWrapper.like("color_name", queryCondition).or().like("color_code", queryCondition);
        }

        List<BoxGoodsColor> goodsColorList = boxGoodsColorService.list(queryWrapper);
        if (goodsColorList == null || goodsColorList.size() == 0) {
            return Result.failure("暂无数据");
        }

        Map<String, String> color = new HashMap<>();
        for (BoxGoodsColor boxGoodsColor : goodsColorList) {
            color.put(boxGoodsColor.getColorCode(), boxGoodsColor.getColorName());
        }

        return Result.success().setData("color", color);
    }
}

