package com.kuose.box.admin.survery.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.survery.service.BoxSurveyQusetionLabelService;
import com.kuose.box.admin.survery.service.BoxSurveyUserAnswerService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.survery.entity.BoxSurveyQusetionLabel;
import com.kuose.box.db.survery.entity.BoxSurveyUserAnswer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    private BoxSurveyQusetionLabelService boxSurveyQusetionLabelService;

    @ApiOperation(value="新增用户问券答案")
    @PostMapping("/add")
    public Result add(@RequestBody BoxSurveyUserAnswer boxSurveyUserAnswer) {
        if (StringUtil.isBlank(boxSurveyUserAnswer.getLabelCode()) || boxSurveyUserAnswer.getOptionValues() == null ||
                boxSurveyUserAnswer.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyUserAnswer.setUpdateTime(System.currentTimeMillis());
        boxSurveyUserAnswer.setCreateTime(System.currentTimeMillis());
        boxSurveyUserAnswerService.save(boxSurveyUserAnswer);
        return Result.success();
    }

    @ApiOperation(value="获取用户档案详情,搭配后台调用")
    @GetMapping("/getUserAnswer")
    public Result getUserAnswer(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }

        List<BoxSurveyQusetionLabel> surveyQusetionLabels = boxSurveyQusetionLabelService.list(new QueryWrapper<BoxSurveyQusetionLabel>().eq("deleted", 0));
        if (surveyQusetionLabels == null || surveyQusetionLabels.size() < 1) {
            return Result.success().setData("surveyQusetionLabels", surveyQusetionLabels);
        }

        List<BoxSurveyUserAnswer> surveyUserAnswers = boxSurveyUserAnswerService.list(new QueryWrapper<BoxSurveyUserAnswer>().eq("user_id", userId));
        for (BoxSurveyQusetionLabel surveyQusetionLabel : surveyQusetionLabels) {
            String labelCode = surveyQusetionLabel.getLabelCode();
            if (surveyUserAnswers != null && surveyUserAnswers.size() >= 1) {
                for (BoxSurveyUserAnswer boxSurveyUserAnswer : surveyUserAnswers) {
                    if (labelCode.equals(boxSurveyUserAnswer.getLabelCode())) {
                        surveyQusetionLabel.setLabelValue(boxSurveyUserAnswer.getOptionValues());
                    }
                }
            }
        }

        return Result.success().setData("surveyQusetionLabels", surveyQusetionLabels);
    }
}

