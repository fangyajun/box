package com.kuose.box.admin.survery.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.survery.entity.BoxSurveyQuestionOptions;
import com.kuose.box.admin.survery.entity.BoxSurveyQusetion;
import com.kuose.box.admin.survery.service.BoxSurveyQuestionOptionsService;
import com.kuose.box.admin.survery.service.BoxSurveyQusetionService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 问题表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Api(tags = {"问券管理，问题管理"})
@RestController
@RequestMapping("/boxSurveyQusetion")
public class BoxSurveyQusetionController {

    @Autowired
    private BoxSurveyQusetionService boxSurveyQusetionService;
    @Autowired
    private BoxSurveyQuestionOptionsService boxSurveyQuestionOptionsService;

    @ApiOperation(value="新增问题")
    @PostMapping("add")
    public Result add(@RequestBody  BoxSurveyQusetion boxSurveyQusetion) {
        if (StringUtil.isBlank(boxSurveyQusetion.getQuestionTopic()) || boxSurveyQusetion.getQuestionType() == null ||
                boxSurveyQusetion.getSurveyId() == null || StringUtil.isBlank(boxSurveyQusetion.getLabelCode())) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQusetion.setCreateTime(System.currentTimeMillis());
        boxSurveyQusetion.setUpdateTime(System.currentTimeMillis());

        boxSurveyQusetionService.save(boxSurveyQusetion);
        return Result.success();
    }

    @ApiOperation(value="问题列表")
    @GetMapping("list")
    public Result list(Integer surveyId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit)

    {
        if (page == null || limit == null) {
            return Result.failure("缺少必传参数");
        }

        Page<BoxSurveyQusetion> boxSurveyQusetionPage = new Page<>();
        boxSurveyQusetionPage.setSize(limit);
        boxSurveyQusetionPage.setCurrent(page);
        boxSurveyQusetionPage.setDesc("create_time");

        IPage<BoxSurveyQusetion> boxSurveyQusetionIPage = boxSurveyQusetionService.page(boxSurveyQusetionPage, new QueryWrapper<BoxSurveyQusetion>().
                eq("is_deleted", 0).eq("survey_id", surveyId));
        return Result.success().setData("boxSurveyQusetionIPage", boxSurveyQusetionIPage);
    }

    @ApiOperation(value="修改问题")
    @PostMapping("update")
    public Result update(@RequestBody  BoxSurveyQusetion boxSurveyQusetion) {
        if (boxSurveyQusetion.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQusetion.setUpdateTime(System.currentTimeMillis());
        boxSurveyQusetionService.updateById(boxSurveyQusetion);
        return Result.success();
    }

    @ApiOperation(value="问题详情")
    @GetMapping("getSurveyQusetion")
    public Result getSurveyQusetion(Integer id) {
        if (id == null) {
            return Result.failure("缺少必传参数");
        }

        BoxSurveyQusetion boxSurveyQusetion = boxSurveyQusetionService.getById(id);
        return Result.success().setData("boxSurveyQusetion", boxSurveyQusetion);
    }

    @ApiOperation(value="删除问题")
    @PostMapping("delete")
    public Result delete(@RequestBody  BoxSurveyQusetion boxSurveyQusetion) {
        if (boxSurveyQusetion.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        int count = boxSurveyQuestionOptionsService.count(new QueryWrapper<BoxSurveyQuestionOptions>().eq("question_id", boxSurveyQusetion.getId()));
        if (count <= 0) {
            return Result.failure(510, "请先删除问题的选项在删除问题");
        }

        // 逻辑删除
        boxSurveyQusetion.setDeleted(1);
        boxSurveyQusetionService.updateById(boxSurveyQusetion);

        return Result.success();
    }
}

