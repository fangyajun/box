package com.kuose.box.wx.survey.controller;


import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.survery.entity.BoxSurveyUserAnswer;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.survey.service.BoxSurveyUserAnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/add") //
    public Result add(@RequestBody BoxSurveyUserAnswer boxSurveyUserAnswer, @ApiParam(hidden = true) @LoginUser Integer userId) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }
        if (StringUtil.isBlank(boxSurveyUserAnswer.getLabelCode()) || boxSurveyUserAnswer.getOptionValues() == null ||
                boxSurveyUserAnswer.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyUserAnswer.setUpdateTime(System.currentTimeMillis());
        boxSurveyUserAnswer.setCreateTime(System.currentTimeMillis());
        boxSurveyUserAnswerService.save(boxSurveyUserAnswer);
        return Result.success();
    }

}

