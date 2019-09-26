package com.kuose.box.wx.survey.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.survey.entity.BoxSurvey;
import com.kuose.box.wx.survey.service.BoxSurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 问卷 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-24
 */
@Api(tags = {"问卷，问卷展示"})
@RestController
@RequestMapping("/boxSurvey")
public class BoxSurveyController {

    @Autowired
    public BoxSurveyService boxSurveyService;

    @ApiOperation(value="获取问卷列表")
    @GetMapping("/list")
    public Result list(@LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        List<BoxSurvey> surveyList = boxSurveyService.list(new QueryWrapper<BoxSurvey>().eq("is_deleted", 0).orderByDesc("sort"));
        return Result.success().setData("surveyList", surveyList);
    }

}

