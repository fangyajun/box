package com.kuose.box.wx.survey.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.survery.entity.BoxUserAnswerJson;
import com.kuose.box.wx.survey.service.BoxUserAnswerJsonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 问卷用户答案josn 前端控制器
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-21
 */
@Api(tags = {"问卷，用户答题json存储"})
@RestController
@RequestMapping("/boxUserAnswerJson")
public class BoxUserAnswerJsonController {

    @Autowired
    private BoxUserAnswerJsonService boxUserAnswerJsonService;

    @ApiOperation(value="新增或修改")
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody BoxUserAnswerJson boxUserAnswerJson) {
        if (boxUserAnswerJson.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxUserAnswerJson userAnswerJson = boxUserAnswerJsonService.getOne(new QueryWrapper<BoxUserAnswerJson>().eq("user_id", boxUserAnswerJson.getUserId()));
        if (userAnswerJson != null) {
            userAnswerJson.setAnswerJson(boxUserAnswerJson.getAnswerJson());
            userAnswerJson.setUpdateTime(System.currentTimeMillis());
            return Result.success();
        }

        // 新增
        boxUserAnswerJson.setCreateTime(System.currentTimeMillis());
        boxUserAnswerJson.setUpdateTime(System.currentTimeMillis());
        boxUserAnswerJsonService.save(boxUserAnswerJson);

        return Result.success();
    }

    @ApiOperation(value="获取用户答案josn")
    @GetMapping("/get")
    public Result get(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxUserAnswerJson userAnswerJson = boxUserAnswerJsonService.getOne(new QueryWrapper<BoxUserAnswerJson>().eq("user_id", userId));

        return Result.success().setData("userAnswerJson", userAnswerJson);
    }



}

