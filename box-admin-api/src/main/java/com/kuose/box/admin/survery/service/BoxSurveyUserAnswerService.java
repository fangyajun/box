package com.kuose.box.admin.survery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.db.survery.entity.BoxSurveyUserAnswer;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷用户答案表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
public interface BoxSurveyUserAnswerService extends IService<BoxSurveyUserAnswer> {

    List<Map<Integer,String>> getQuestionShort(Integer id);

    List<Map<Integer,String>> listUserQuestionAnswer(Integer id);

    List<Map<Integer,String>> getOptionQuestionShort(Integer id);

    List<Map<Integer,String>> listUserOptionQuestionAnswer(Integer id);
}
