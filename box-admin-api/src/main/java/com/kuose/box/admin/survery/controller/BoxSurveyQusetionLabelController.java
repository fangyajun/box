package com.kuose.box.admin.survery.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.survery.service.BoxSurveyQusetionLabelService;
import com.kuose.box.admin.survery.service.BoxSurveyQusetionService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.survery.entity.BoxSurveyQusetion;
import com.kuose.box.db.survery.entity.BoxSurveyQusetionLabel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 问卷问题标签 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-02
 */
@Api(tags = {"问券管理，问题标签管理"})
@RestController
@RequestMapping("/boxSurveyQusetionLabel")
public class BoxSurveyQusetionLabelController {

    @Autowired
    private BoxSurveyQusetionLabelService boxSurveyQusetionLabelService;
    @Autowired
    private BoxSurveyQusetionService boxSurveyQusetionService;

    @ApiOperation(value="新增问题标签")
    @PostMapping("/add")
    public Result add(@RequestBody BoxSurveyQusetionLabel boxSurveyQusetionLabel) {
        if (StringUtil.isBlank(boxSurveyQusetionLabel.getLabelName()) || StringUtil.isBlank(boxSurveyQusetionLabel.getLabelCode())) {
            return Result.failure("缺少必传参数");
        }

        int count1 = boxSurveyQusetionLabelService.count(new QueryWrapper<BoxSurveyQusetionLabel>().eq("deleted", 0).
                    eq("label_code", boxSurveyQusetionLabel.getLabelCode()));
        int count2 = boxSurveyQusetionLabelService.count(new QueryWrapper<BoxSurveyQusetionLabel>().eq("deleted", 0).
                    eq("label_name", boxSurveyQusetionLabel.getLabelName()));
        if (count1 >= 1 || count2 >= 1) {
            return Result.failure("已存在标签名称或者标签编码！");
        }

        boxSurveyQusetionLabel.setLabelType(0);
        boxSurveyQusetionLabel.setUpdateTime(System.currentTimeMillis());
        boxSurveyQusetionLabel.setCreateTime(System.currentTimeMillis());
        boxSurveyQusetionLabelService.save(boxSurveyQusetionLabel);
        return Result.success();
    }

    @ApiOperation(value="更新问题标签")
    @PostMapping("/update")
    public Result update(@RequestBody BoxSurveyQusetionLabel boxSurveyQusetionLabel) {
        if (boxSurveyQusetionLabel.getId() == null) {
            return Result.failure("缺少必传参数");
        }
        int count = boxSurveyQusetionLabelService.count(new QueryWrapper<BoxSurveyQusetionLabel>().ne("id", boxSurveyQusetionLabel.getId()).eq("deleted", 0).
                eq("label_code", boxSurveyQusetionLabel.getLabelCode()).or().eq("label_name", boxSurveyQusetionLabel.getLabelName()));
        if (count >= 1) {
            return Result.failure("已存在标签名称或者标签编码！");
        }

        boxSurveyQusetionLabelService.updateById(boxSurveyQusetionLabel);
        return Result.success();
    }

    @ApiOperation(value="问题标签列表")
    @GetMapping("/list")
    public Result list(String labelName, Integer lableType) {
        QueryWrapper<BoxSurveyQusetionLabel> labelQueryWrapper = new QueryWrapper<BoxSurveyQusetionLabel>().eq("deleted", 0);
        if (!StringUtil.isBlank(labelName)) {
            labelQueryWrapper.like("label_name", labelName);
        }
        if (lableType != null) {
            labelQueryWrapper.eq("label_type", lableType);
        }

        List<BoxSurveyQusetionLabel> qusetionLabelList = boxSurveyQusetionLabelService.list(labelQueryWrapper);
        // 判断标签是否已被问题添加
        if (qusetionLabelList != null && qusetionLabelList.size() >= 1) {
            for (BoxSurveyQusetionLabel boxSurveyQusetionLabel : qusetionLabelList) {
                int count = boxSurveyQusetionService.count(new QueryWrapper<BoxSurveyQusetion>().eq("is_deleted", 0).
                        eq("label_code", boxSurveyQusetionLabel.getLabelCode()));
                if (count >= 1) {
                    boxSurveyQusetionLabel.setStatus(1);
                }else {
                    boxSurveyQusetionLabel.setStatus(0);
                }
            }
        }

        return Result.success().setData("qusetionLabelList", qusetionLabelList);
    }

    @ApiOperation(value="删除问题标签")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxSurveyQusetionLabel boxSurveyQusetionLabel) {
        if (boxSurveyQusetionLabel.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxSurveyQusetionLabel.setDeleted(1);
        boxSurveyQusetionLabelService.updateById(boxSurveyQusetionLabel);
        return Result.success();
    }
}

