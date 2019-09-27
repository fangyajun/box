package com.kuose.box.admin.survery.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.survery.service.BoxSurveyQuestionOptionsService;
import com.kuose.box.admin.survery.service.BoxSurveyQusetionService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.survery.entity.BoxSurveyQuestionOptions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 问题选项表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Api(tags = {"问券管理，问题选项管理"})
@RestController
@RequestMapping("/boxSurveyQuestionOptions")
public class BoxSurveyQuestionOptionsController {

    @Autowired
    private BoxSurveyQuestionOptionsService boxSurveyQuestionOptionsService;
    @Autowired
    private BoxSurveyQusetionService boxSurveyQusetionService;

    @ApiOperation(value="新增问题选项")
    @PostMapping("add")
    public Result add(@RequestBody BoxSurveyQuestionOptions boxSurveyQuestionOptions) {
        if (boxSurveyQuestionOptions.getQuestionId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQuestionOptions.setCreateTime(System.currentTimeMillis());
        boxSurveyQuestionOptions.setUpdateTime(System.currentTimeMillis());
        boxSurveyQuestionOptionsService.save(boxSurveyQuestionOptions);
        return Result.success();
    }


    @ApiOperation(value="问题选项列表")
    @GetMapping("list")
    public Result list(Integer questionId) {
        if (questionId == null) {
            return Result.failure("缺少必传参数");
        }
        List<BoxSurveyQuestionOptions> boxSurveyQuestionOptionsList = boxSurveyQuestionOptionsService.list(new QueryWrapper<BoxSurveyQuestionOptions>().eq("question_id", questionId).
                eq("is_deleted", 0));
        return Result.success().setData("boxSurveyQuestionOptionsList", boxSurveyQuestionOptionsList);
    }

    @ApiOperation(value="修改问题选项")
    @PostMapping("update")
    public Result update(@RequestBody BoxSurveyQuestionOptions boxSurveyQuestionOptions) {
        if (boxSurveyQuestionOptions.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQuestionOptions.setUpdateTime(System.currentTimeMillis());
        boxSurveyQuestionOptionsService.updateById(boxSurveyQuestionOptions);
        return Result.success();
    }

    @ApiOperation(value="删除问题选项")
    @PostMapping("delete")
    public Result delete(@RequestBody BoxSurveyQuestionOptions boxSurveyQuestionOptions) {
        if (boxSurveyQuestionOptions.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQuestionOptions.setDeleted(1);
        boxSurveyQuestionOptionsService.updateById(boxSurveyQuestionOptions);
        return Result.success();
    }

}

