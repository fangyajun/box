package com.kuose.box.wx.survey.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.survey.entity.BoxSurveyQuestionOptions;
import com.kuose.box.wx.survey.entity.BoxSurveyQusetion;
import com.kuose.box.wx.survey.entity.BoxSurveyUserAnswer;
import com.kuose.box.wx.survey.service.BoxSurveyQuestionOptionsService;
import com.kuose.box.wx.survey.service.BoxSurveyQusetionService;
import com.kuose.box.wx.survey.service.BoxSurveyUserAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>
 * 问题表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-24
 */
@Api(tags = {"问卷，问题展示"})
@Controller
@RequestMapping("/boxSurveyQusetion")
public class BoxSurveyQusetionController {

    @Autowired
    private BoxSurveyQusetionService boxSurveyQusetionService;
    @Autowired
    private BoxSurveyQuestionOptionsService boxSurveyQuestionOptionsService;
    @Autowired
    private BoxSurveyUserAnswerService boxSurveyUserAnswerService;

    @ApiOperation(value="获取问题列表")
    @GetMapping("/list")
    public Result list(@LoginUser Integer userId, Integer surveyId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }

        QueryWrapper<BoxSurveyQusetion> queryWrapper = new QueryWrapper<BoxSurveyQusetion>().eq("is_deleted", 0).orderByDesc("sort");
        if (surveyId != null) {
            queryWrapper.eq("survey_id", surveyId);
        }

        List<BoxSurveyQusetion> qusetionList = boxSurveyQusetionService.list(queryWrapper);
        if (qusetionList == null || qusetionList.isEmpty()) {
            return Result.success().setData("qusetionList", qusetionList);
        }


        for (BoxSurveyQusetion boxSurveyQusetion : qusetionList) {
            // 添加问题选项
            List<BoxSurveyQuestionOptions> optionsList = boxSurveyQuestionOptionsService.list(new QueryWrapper<BoxSurveyQuestionOptions>().eq("is_deleted", 0).
                    eq("question_id", boxSurveyQusetion.getId()).orderByDesc("sort"));
            boxSurveyQusetion.setOptionsList(optionsList);

            // 问题答案
            List<BoxSurveyUserAnswer> userAnswerList = boxSurveyUserAnswerService.list(new QueryWrapper<BoxSurveyUserAnswer>().
                    eq("user_id", userId).eq("question_id", boxSurveyQusetion.getId()));

            boxSurveyQusetion.setUserAnswerList(userAnswerList);
        }

        return Result.success().setData("qusetionList", qusetionList);
    }
}

