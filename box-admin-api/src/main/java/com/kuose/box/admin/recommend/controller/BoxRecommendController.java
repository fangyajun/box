package com.kuose.box.admin.recommend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.admin.recommend.service.BoxRecommendGoodsService;
import com.kuose.box.admin.recommend.service.BoxRecommendService;
import com.kuose.box.admin.recommend.service.BoxUserRecommendService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.recommend.entity.BoxRecommend;
import com.kuose.box.db.recommend.entity.BoxRecommendGoods;
import com.kuose.box.db.recommend.entity.BoxUserRecommend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * box_user_recommend 前端控制器
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Api(tags = {"用户推荐，推荐搭配单"})
@RestController
@RequestMapping("/boxRecommend")
public class BoxRecommendController {

    @Autowired
    private BoxRecommendService boxRecommendService;
    @Autowired
    private BoxRecommendGoodsService boxRecommendGoodsService;
    @Autowired
    private BoxUserRecommendService boxUserRecommendService;

    @ApiOperation(value="新增用户推荐搭配单，传用户id, 一个用户可以有多个搭配单")
    @PostMapping("/add")
    public Result add(@RequestBody BoxRecommend boxRecommend) {
        if (boxRecommend.getUserId() == null || boxRecommend.getUserRecommendId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxRecommend.setAuditStatus(0);
        boxRecommend.setCommentStatus(0);
        boxRecommend.setCreateTime(System.currentTimeMillis());
        boxRecommend.setUpdateTime(System.currentTimeMillis());

        boxRecommendService.save(boxRecommend);
        return Result.success().setData("boxRecommend", boxRecommend);
    }

    @ApiOperation(value="搭配单排序")
    @PostMapping("/sort")
    public Result sort(@RequestBody BoxRecommend boxRecommend) {
        if (boxRecommend.getId() == null || boxRecommend.getSort() == null) {
            return Result.failure("缺少必传参数");
        }

        boxRecommend.setUpdateTime(System.currentTimeMillis());
        boxRecommendService.updateById(boxRecommend);
        return Result.success();
    }

    @ApiOperation(value="删除，传id")
    @PostMapping("/delete")
    public Result delete(@RequestBody BoxRecommend boxRecommend) {
        if (boxRecommend.getId() == null) {
            return Result.failure("缺少必传参数");
        }

        boxRecommendGoodsService.remove(new QueryWrapper<BoxRecommendGoods>().eq("box_recommend_id", boxRecommend.getId()));
        boxRecommendService.removeById(boxRecommend.getId());
        return Result.success();
    }

    @ApiOperation(value="审核搭配单")
    @PostMapping("/audit")
    public Result audit(@RequestBody BoxRecommend boxRecommend) {
        if (boxRecommend.getId() == null && boxRecommend.getAuditStatus() == null) {
            return Result.failure("缺少必传参数");
        }

        List<BoxRecommendGoods> recommendGoodsList = boxRecommendGoodsService.list(new QueryWrapper<BoxRecommendGoods>().eq("box_recommend_id", boxRecommend.getId()));
        if (recommendGoodsList == null || recommendGoodsList.isEmpty()) {
            return Result.failure("该搭配单还未进行搭配，请先搭配在审核");
        }

        BoxRecommend recommend = boxRecommendService.getById(boxRecommend.getId());
        if (recommend == null) {
            return Result.failure("数据异常，该搭配单不存在");
        }

        if (recommend.getAuditStatus() == 1) {
            return Result.failure("该搭配单已审核通过");
        }

        recommend.setAuditStatus(boxRecommend.getAuditStatus());
        recommend.setUpdateTime(System.currentTimeMillis());
        boxRecommendService.updateById(recommend);

        if (boxRecommend.getAuditStatus() == 1) {
            // 修改用户推荐表推荐状态为已推荐
            BoxUserRecommend boxUserRecommend = boxUserRecommendService.getById(recommend.getUserRecommendId());
            boxUserRecommend.setRecommendStatus(2);
            boxUserRecommendService.updateById(boxUserRecommend);
        }

        return Result.success();
    }

    @ApiOperation(value="取消审核,只需要传id")
    @PostMapping("/cancelAudit")
    public Result cancelAudit(@RequestBody BoxRecommend boxRecommend) {
        if (boxRecommend.getId() == null && boxRecommend.getAuditStatus() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxRecommend recommend = boxRecommendService.getById(boxRecommend.getId());
        if (recommend == null) {
            return Result.failure("数据异常，该搭配单不存在");
        }

        if (recommend.getAuditStatus() == 0) {
            return Result.failure("该搭配没有审核，无法取消审核");
        }

        recommend.setAuditStatus(0);
        recommend.setUpdateTime(System.currentTimeMillis());
        boxRecommendService.updateById(recommend);
        return Result.success();
    }

}

