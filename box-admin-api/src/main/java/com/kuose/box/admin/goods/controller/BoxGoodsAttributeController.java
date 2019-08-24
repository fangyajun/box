package com.kuose.box.admin.goods.controller;


import com.kuose.box.admin.goods.dto.GoodsAllinone;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeService;
import com.kuose.box.common.config.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品参数表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@RestController
@RequestMapping("/boxGoodsAttribute")
public class BoxGoodsAttributeController {

    @Autowired
    private BoxGoodsAttributeService boxGoodsAttributeService;


    @PostMapping("/add")
    public Result add(@RequestBody GoodsAllinone goodsAllinone) {
        boxGoodsAttributeService.save(goodsAllinone);

        return Result.success();
    }

}

