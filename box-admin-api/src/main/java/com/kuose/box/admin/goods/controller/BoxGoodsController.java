package com.kuose.box.admin.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.goods.dto.GoodsAllinone;
import com.kuose.box.admin.goods.dto.GoodsQueryParameter;
import com.kuose.box.admin.goods.entity.BoxGoods;
import com.kuose.box.admin.goods.entity.BoxGoodsAttribute;
import com.kuose.box.admin.goods.entity.BoxGoodsSku;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeService;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value="添加商品")
    @PostMapping("/add")
    public Result add(@RequestBody GoodsAllinone goodsAllinone) {
        if (goodsAllinone.getBoxGoods() == null || goodsAllinone.getBoxGoodsAttributes() == null || goodsAllinone.getBoxGoodsSkus() == null) {
            return Result.failure("缺少参数");
        }
        if (StringUtil.isBlank(goodsAllinone.getBoxGoods().getName())) {
            return Result.failure("缺少商品基本数据");
        }
        if ( goodsAllinone.getBoxGoodsAttributes().length <= 0 || goodsAllinone.getBoxGoodsSkus().length <= 0) {
            return Result.failure("缺少商品基本属性");
        }

        Result result = boxGoodsService.save(goodsAllinone);
        return result;
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

    @ApiOperation(value="商品列表")
    @GetMapping("list")
    public Result list(GoodsQueryParameter goodsQueryParameter, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Page<BoxGoods> adminPage = new Page<>();
        adminPage.setSize(limit);
        adminPage.setCurrent(page);

        IPage<BoxGoods> boxGoodsIPage = boxGoodsService.listGoodsPage(adminPage, goodsQueryParameter);
        return Result.success().setData("boxGoodsIPage", boxGoodsIPage);
    }

    @ApiOperation(value="商品详情")
    @GetMapping("/detail")
    public Result detail(Integer id) {
        if (id == null) {
            return Result.failure("参数必传");
        }
        BoxGoods boxGoods = boxGoodsService.getById(id);
        List<BoxGoodsAttribute> goodsAttributeList = boxGoodsAttributeService.list(new QueryWrapper<BoxGoodsAttribute>().eq("goods_id", id).eq("deleted", 0));
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
            return Result.failure("缺少商品基本数据");
        }
        if ( goodsAllinone.getBoxGoodsAttributes().length <= 0 || goodsAllinone.getBoxGoodsSkus().length <= 0) {
            return Result.failure("缺少商品基本属性");
        }

        Result result = boxGoodsService.update(goodsAllinone);
        return result;
    }

}

