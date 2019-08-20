package com.kuose.box.admin.survery.controller;


import com.kuose.box.admin.survery.service.BoxSurveyUserAnswerService;
import com.kuose.box.common.config.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷用户答案表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Api(tags = {"问券管理，用户问题答案"})
@RestController
@RequestMapping("/boxSurveyUserAnswer")
public class BoxSurveyUserAnswerController {

    @Autowired
    private BoxSurveyUserAnswerService boxSurveyUserAnswerService;

    @ApiOperation(value="获取填空问题简称列表和答案")
    @GetMapping("questionShortList")
    public Result getQuestionShort(Integer id) {
        List<Map<Integer,String>> questionShortList = boxSurveyUserAnswerService.getQuestionShort(id);
        List<Map<Integer,String>> listBoxSurveyUserAnswer = boxSurveyUserAnswerService.listUserQuestionAnswer(id);
        return Result.success().setData("questionShortList", questionShortList).setData("listBoxSurveyUserAnswer", listBoxSurveyUserAnswer);
    }

    @ApiOperation(value="获取选择问题简称列表和答案")
    @GetMapping("/getOptionQuestionShort")
    public Result getOptionQuestionShort(Integer id) {
        List<Map<Integer,String>> boxSurveyUserAnswerServiceOptionQuestionShort = boxSurveyUserAnswerService.getOptionQuestionShort(id);
        List<Map<Integer,String>> listUserOptionQuestionAnswer = boxSurveyUserAnswerService.listUserOptionQuestionAnswer(id);
        return Result.success().setData("boxSurveyUserAnswerServiceOptionQuestionShort", boxSurveyUserAnswerServiceOptionQuestionShort).
                setData("listUserOptionQuestionAnswer", listUserOptionQuestionAnswer);
    }

}

