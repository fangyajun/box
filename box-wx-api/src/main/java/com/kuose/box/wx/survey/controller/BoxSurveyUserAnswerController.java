package com.kuose.box.wx.survey.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.survery.entity.BoxSurveyUserAnswer;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.survey.service.BoxSurveyUserAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 问卷用户答案表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-24
 */
@Api(tags = {"问卷，用户答题"})
@RestController
@RequestMapping("/boxSurveyUserAnswer")
public class BoxSurveyUserAnswerController {

    @Autowired
    private BoxSurveyUserAnswerService boxSurveyUserAnswerService;

    @ApiOperation(value="新增用户问券答案")
    @PostMapping("/add")
    public Result add(@RequestBody BoxSurveyUserAnswer boxSurveyUserAnswer, @ApiParam(hidden = true) @LoginUser Integer userId) {
        if (userId == null) {
            return Result.failure(501, "请登录");
        }
        if (StringUtil.isBlank(boxSurveyUserAnswer.getLabelCode()) || boxSurveyUserAnswer.getOptionValues() == null ||
                boxSurveyUserAnswer.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxSurveyUserAnswer surveyUserAnswer = boxSurveyUserAnswerService.getOne(new QueryWrapper<BoxSurveyUserAnswer>().
                eq("user_id", boxSurveyUserAnswer.getUserId()).eq("label_code", boxSurveyUserAnswer.getLabelCode()));
        if (surveyUserAnswer != null) {
            surveyUserAnswer.setQuestionId(boxSurveyUserAnswer.getQuestionId());
            surveyUserAnswer.setOptionIds(boxSurveyUserAnswer.getOptionIds());
            surveyUserAnswer.setQuestionInfo(boxSurveyUserAnswer.getQuestionInfo());
            surveyUserAnswer.setOptionValues(boxSurveyUserAnswer.getOptionValues());
            surveyUserAnswer.setOptionContent(boxSurveyUserAnswer.getOptionContent());
            surveyUserAnswer.setUpdateTime(System.currentTimeMillis());

            boxSurveyUserAnswerService.updateById(surveyUserAnswer);
            return Result.success();
        }

        boxSurveyUserAnswer.setUpdateTime(System.currentTimeMillis());
        boxSurveyUserAnswer.setCreateTime(System.currentTimeMillis());
        boxSurveyUserAnswerService.save(boxSurveyUserAnswer);
        return Result.success();
    }

    @ApiOperation(value="获取用户答题答案，labelCode参数是问题的标签，如传入就查具体一个问题的答案，不传查全部")
    @GetMapping("/getUserAnswer")
    public Result getUserAnswer(Integer userId, String labelCode) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }

        QueryWrapper<BoxSurveyUserAnswer> answerQueryWrapper = new QueryWrapper<BoxSurveyUserAnswer>().eq("user_id", userId);
        if (!StringUtil.isBlank(labelCode)) {
            answerQueryWrapper.eq("label_code", labelCode);
        }

        List<BoxSurveyUserAnswer> userAnswerList = boxSurveyUserAnswerService.list(answerQueryWrapper);

        return Result.success().setData("userAnswerList", userAnswerList);
    }

}

