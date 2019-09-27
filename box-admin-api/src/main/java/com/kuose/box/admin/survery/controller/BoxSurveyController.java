package com.kuose.box.admin.survery.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.survery.service.BoxSurveyService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.survery.entity.BoxSurvey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 问卷 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Api(tags = {"问券管理，问券管理"})
@RestController
@RequestMapping("/boxSurvey")
public class BoxSurveyController {

    @Autowired
    private BoxSurveyService boxSurveyService;

    @ApiOperation(value="新增问券")
    @PostMapping("/add")
    public Result add(@RequestBody BoxSurvey boxSurvey) {
        if (boxSurvey == null) {
            return Result.failure("缺少必传参数");
        }

        if (StringUtil.isBlank(boxSurvey.getSurveyName())) {
            return Result.failure("缺少必传参数");
        }

        boxSurvey.setCreateTime(System.currentTimeMillis());
        boxSurvey.setUpdateTime(System.currentTimeMillis());
        boxSurveyService.save(boxSurvey);

        return Result.success();
    }

    @ApiOperation(value="修改问券")
    @PostMapping("/update")
    public Result update(@RequestBody BoxSurvey boxSurvey) {
        if (boxSurvey.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurvey.setUpdateTime(System.currentTimeMillis());
        boxSurveyService.updateById(boxSurvey);

        return Result.success();
    }

    @ApiOperation(value="问券列表")
    @GetMapping("/list")
    public Result list (@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        if (page == null || limit == null) {
            return Result.failure("缺少必传参数");
        }

        Page<BoxSurvey> surveyPage = new Page<>();
        surveyPage.setSize(limit);
        surveyPage.setCurrent(page);
        surveyPage.setDesc("create_time");

        IPage<BoxSurvey> boxSurveyIPage = boxSurveyService.page(surveyPage, new QueryWrapper<BoxSurvey>().eq("is_deleted", 0));
        return Result.success().setData("boxSurveyIPage", boxSurveyIPage);
    }

    @ApiOperation(value="获取问券详情")
    @GetMapping("getSurvey")
    public Result getSurvey(Integer id) {
        if (id == null) {
            return Result.failure("缺少必传参数");
        }

        BoxSurvey boxSurvey = boxSurveyService.getById(id);
        return Result.success().setData("boxSurvey", boxSurvey);
    }

    @ApiOperation(value="删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxSurvey boxSurvey) {
        if (boxSurvey.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        // 逻辑删除
        boxSurvey.setDeleted(1);
        boxSurveyService.updateById(boxSurvey);

        return Result.success();
    }
}

