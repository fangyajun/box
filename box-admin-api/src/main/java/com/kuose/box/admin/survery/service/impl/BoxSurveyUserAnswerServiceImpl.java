package com.kuose.box.admin.survery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.survery.service.BoxSurveyUserAnswerService;
import com.kuose.box.db.survery.dao.BoxSurveyUserAnswerMapper;
import com.kuose.box.db.survery.entity.BoxSurveyUserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷用户答案表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-31
 */
@Service
public class BoxSurveyUserAnswerServiceImpl extends ServiceImpl<BoxSurveyUserAnswerMapper, BoxSurveyUserAnswer> implements BoxSurveyUserAnswerService {

    @Autowired
    private BoxSurveyUserAnswerMapper boxSurveyUserAnswerMapper;

    @Override
    public List<Map<Integer, String>> getQuestionShort(Integer id) {

        return boxSurveyUserAnswerMapper.getQuestionShort(id);
    }

    @Override
    public List<Map<Integer, String>> listUserQuestionAnswer(Integer id) {
        return boxSurveyUserAnswerMapper.listUserQuestionAnswer(id);
    }

    @Override
    public List<Map<Integer, String>> getOptionQuestionShort(Integer id) {
        return boxSurveyUserAnswerMapper.getOptionQuestionShort(id);
    }

    @Override
    public List<Map<Integer, String>> listUserOptionQuestionAnswer(Integer id) {
        return boxSurveyUserAnswerMapper.listUserOptionQuestionAnswer(id);
    }
}
