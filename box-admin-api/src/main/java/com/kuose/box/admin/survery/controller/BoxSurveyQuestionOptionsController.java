package com.kuose.box.admin.survery.controller;

import com.kuose.box.admin.survery.service.BoxSurveyQuestionOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 问题选项表 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@RestController
@RequestMapping("/boxSurveyQuestionOptions")
public class BoxSurveyQuestionOptionsController {

    @Autowired
    private BoxSurveyQuestionOptionsService boxSurveyQuestionOptionsService;


}

