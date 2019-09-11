package com.kuose.source.goods.controller;

import com.kuose.box.common.config.Result;
import com.kuose.source.goods.entity.AttributeSource;
import com.kuose.source.goods.service.RipreportProductinformationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/11
 */
@Api(tags = {"商品属性"})
@RestController
@RequestMapping("/ripreportProductinformation")
public class AttributeController {

    @Autowired
    private RipreportProductinformationService ripreportProductinformationService;

    @GetMapping("/listAllAttibutes")
    public Result listAllAttibutes() {
        List<AttributeSource> attributeSourceList = ripreportProductinformationService.listAllAttibutes();
        return Result.success().setData("attributeSourceList", attributeSourceList);
    }


}
