package com.kuose.box.admin.survery.controller;


import com.kuose.box.admin.survery.service.BoxSurveyQuestionViewTypeService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.survery.entity.BoxSurveyQuestionViewType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>
 * 问题视图类别表 前端控制器
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-10-09
 */
@Api(tags = {"问券管理，前端视图类别"})
@Controller
@RequestMapping("/boxSurveyQuestionViewType")
public class BoxSurveyQuestionViewTypeController {

    @Autowired
    private BoxSurveyQuestionViewTypeService boxSurveyQuestionViewTypeService;

    @ApiOperation(value="新增视图类别")
    @PostMapping("/add")
    public Result add(@RequestBody BoxSurveyQuestionViewType boxSurveyQuestionViewType) {
        if (StringUtil.isBlank(boxSurveyQuestionViewType.getViewTypeName())) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQuestionViewType.setCreateTime(System.currentTimeMillis());
        boxSurveyQuestionViewType.setUpdateTime(System.currentTimeMillis());

        boxSurveyQuestionViewTypeService.save(boxSurveyQuestionViewType);
        return Result.success();
    }

    @ApiOperation(value="删除视图类别")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxSurveyQuestionViewType boxSurveyQuestionViewType) {
        if (boxSurveyQuestionViewType.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQuestionViewTypeService.removeById(boxSurveyQuestionViewType.getId());
        return Result.success();
    }

    @ApiOperation(value="修改视图类别")
    @PostMapping("/update")
    public Result update (@RequestBody BoxSurveyQuestionViewType boxSurveyQuestionViewType) {
        if (boxSurveyQuestionViewType.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQuestionViewType.setUpdateTime(System.currentTimeMillis());
        boxSurveyQuestionViewTypeService.updateById(boxSurveyQuestionViewType);
        return Result.success();
    }

    @ApiOperation(value="视图类别列表")
    @PostMapping("/list")
    public Result list() {
        List<BoxSurveyQuestionViewType> viewTypeList = boxSurveyQuestionViewTypeService.list();
        return Result.success().setData("viewTypeList", viewTypeList);
    }

}

