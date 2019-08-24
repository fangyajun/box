package com.kuose.box.admin.goods.controller;


import com.kuose.box.admin.goods.dto.GoodsAllinone;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.common.config.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value="添加商品")
    @PostMapping("/add")
    public Result add(@RequestBody GoodsAllinone goodsAllinone) {
        boxGoodsService.save(goodsAllinone);

        return Result.success();
    }

}

