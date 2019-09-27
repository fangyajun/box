package com.kuose.box.admin.prepay.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.prepay.service.BoxPrepayCardService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.prepay.entity.BoxPrepayCard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 预付金或服务卡表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-26
 */
@Api(tags = {"预付金服务卡管理，预付金服务卡管理"})
@RestController
@RequestMapping("/boxPrepayCard")
public class BoxPrepayCardController {

    @Autowired
    private BoxPrepayCardService prepayCardServic;

    @ApiOperation(value="新增预付金或者服务卡")
    @PostMapping("/add")
    public Result add(@RequestBody BoxPrepayCard boxPrepayCard) {
        if (StringUtil.isBlank(boxPrepayCard.getPrepayName()) || boxPrepayCard.getRetailPrice() == null || boxPrepayCard.getCategory() == null) {
            return Result.failure("缺少必传参数");
        }

        int count = prepayCardServic.count(new QueryWrapper<BoxPrepayCard>().eq("deleted", 0).eq("prepay_name", boxPrepayCard.getPrepayName()));
        if (count >= 1) {
            return Result.failure("名称已存在，请勿重复添加");
        }

        boxPrepayCard.setAddTime(System.currentTimeMillis());
        boxPrepayCard.setUpdateTime(System.currentTimeMillis());

        prepayCardServic.save(boxPrepayCard);
        return Result.success();
    }

    @ApiOperation(value="预付金或者服务卡列表")
    @GetMapping("/list")
    public Result list() {
        List<BoxPrepayCard> prepayCardList = prepayCardServic.list(new QueryWrapper<BoxPrepayCard>().eq("deleted", 0).orderByDesc("add_time"));
        return Result.success().setData("prepayCardList", prepayCardList);
    }

    @ApiOperation(value="是否启用,编辑")
    @PostMapping("/update")
    public Result update(@RequestBody BoxPrepayCard boxPrepayCard) {
        if (boxPrepayCard.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxPrepayCard queryPrepayCard = prepayCardServic.getById(boxPrepayCard.getId());
        if (queryPrepayCard.getCategory() == 0 && boxPrepayCard.getStatus() == 1) {
            // 预付金只能有一个启用
            QueryWrapper<BoxPrepayCard> queryWrapper = new QueryWrapper<BoxPrepayCard>().eq("deleted", 0).
                    eq("category", 0).ne("id", boxPrepayCard.getId()).eq("status", 1);
            if (prepayCardServic.count(queryWrapper) >= 1) {
                return Result.failure(502, "预付金同时只能有一个启用状态");
            }
        }

        boxPrepayCard.setUpdateTime(System.currentTimeMillis());
        prepayCardServic.updateById(boxPrepayCard);
        return Result.success();
    }

    @ApiOperation(value="删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxPrepayCard boxPrepayCard) {
        if (boxPrepayCard.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxPrepayCard.setDeleted(1);
        boxPrepayCard.setUpdateTime(System.currentTimeMillis());
        prepayCardServic.updateById(boxPrepayCard);
        return Result.success();
    }

}

