package com.kuose.box.admin.user.controller;


import com.kuose.box.admin.survery.service.BoxSurveyQusetionLabelService;
import com.kuose.box.admin.survery.service.BoxSurveyUserAnswerService;
import com.kuose.box.admin.user.entity.BoxUser;
import com.kuose.box.admin.user.service.BoxUserService;
import com.kuose.box.common.config.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * box_user 前端控制器
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-04
 */
@RestController
@RequestMapping("/boxUser")
public class BoxUserController {

    @Autowired
    private BoxUserService boxUserService;


    @GetMapping("/getUser")
    public Result getUser(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }
        BoxUser user = boxUserService.getById(userId);
        return Result.success().setData("user",user);
    }




}

