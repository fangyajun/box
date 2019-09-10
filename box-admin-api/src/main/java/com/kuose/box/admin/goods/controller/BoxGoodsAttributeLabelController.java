package com.kuose.box.admin.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.goods.entity.BoxGoodsAttributeLabel;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeLabelService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.PinYinUtils;
import com.kuose.box.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value="添加商品属性标签")
    @PostMapping("/add")
    public Result add(@RequestBody BoxGoodsAttributeLabel boxGoodsAttributeLabel) {
        if (boxGoodsAttributeLabel.getParentId() == null || StringUtil.isBlank(boxGoodsAttributeLabel.getAttributeName())) {
            return Result.failure("缺少必传参数");
        }

        // 头节点的名称不能一样
        if (boxGoodsAttributeLabel.getParentId() == 0) {
            int count = boxGoodsAttributeLabelService.count(new QueryWrapper<BoxGoodsAttributeLabel>().eq("attribute_name", boxGoodsAttributeLabel.getAttributeName()).eq("deleted", 0));
            if (count >= 1) {
                return Result.failure("该参数名称已存在，请勿重复添加！");
            }
        }

        // 生成简码
        String attributeCode = "";
        int count = 0;
        do {
            attributeCode = PinYinUtils.changeToGetShortPinYin(boxGoodsAttributeLabel.getAttributeName()).toUpperCase() + PinYinUtils.getRandomString(3);
            count = boxGoodsAttributeLabelService.count(new QueryWrapper<BoxGoodsAttributeLabel>().eq("attribute_code", attributeCode).eq("deleted", 0));
        } while ( count >= 1);

        boxGoodsAttributeLabel.setAttributeCode(attributeCode);
        boxGoodsAttributeLabel.setAddTime(System.currentTimeMillis());
        boxGoodsAttributeLabel.setUpdateTime(System.currentTimeMillis());

        boxGoodsAttributeLabelService.save(boxGoodsAttributeLabel);
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
    @PostMapping
    public Result delete(@RequestBody BoxGoodsAttributeLabel boxGoodsAttributeLabel) {
        if (boxGoodsAttributeLabel.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxGoodsAttributeLabel.setDeleted(1);
        boxGoodsAttributeLabelService.updateById(boxGoodsAttributeLabel);
        return Result.success();
    }
}

