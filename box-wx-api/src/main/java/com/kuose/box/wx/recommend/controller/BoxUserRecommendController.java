package com.kuose.box.wx.recommend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.recommend.entity.BoxRecommend;
import com.kuose.box.db.recommend.entity.BoxRecommendComment;
import com.kuose.box.db.recommend.entity.BoxRecommendGoods;
import com.kuose.box.db.recommend.entity.BoxUserRecommend;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.recommend.service.BoxRecommendCommentService;
import com.kuose.box.wx.recommend.service.BoxRecommendGoodsService;
import com.kuose.box.wx.recommend.service.BoxRecommendService;
import com.kuose.box.wx.recommend.service.BoxUserRecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户推荐表 前端控制器
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Api(tags = {"用户推荐api"})
@RestController
@RequestMapping("/boxUserRecommend")
public class BoxUserRecommendController {

    @Autowired
    private BoxUserRecommendService boxUserRecommendService;
    @Autowired
    private BoxRecommendService boxRecommendService;
    @Autowired
    private BoxRecommendGoodsService boxRecommendGoodsService;
    @Autowired
    private BoxRecommendCommentService boxRecommendCommentService;

    @ApiOperation(value="创建需要推荐搭配方案的用户，新增只需要传用户id,用户答完问券可以调用此接口")
    @PostMapping("/add")
    public Result add(@RequestBody BoxUserRecommend boxUserRecommend, @ApiParam(hidden = true) @LoginUser Integer userId) {
//        if (userId == null) {
//            return Result.failure(501, "请登录");
//        }

        if (boxUserRecommend.getUserId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxUserRecommend userRecommend = boxUserRecommendService.getOne(new QueryWrapper<BoxUserRecommend>().
                eq("deleted", 0).eq("user_id", boxUserRecommend.getUserId()));
        if (userRecommend != null) {
            if ( userRecommend.getRecommendStatus() == 0 || userRecommend.getRecommendStatus() == 1) {
                return Result.failure("您的搭配方案正在搭配中，请您耐心等候！");
            }

            return Result.failure("已给您搭配推荐商品！请点击我的推荐查看");
        }

        // 没有搭配，新增
        boxUserRecommend.setRecommendStatus(0);
        boxUserRecommend.setCreateTime(System.currentTimeMillis());
        boxUserRecommend.setUpdateTime(System.currentTimeMillis());

        boxUserRecommendService.save(boxUserRecommend);
        return Result.success();
    }

    @ApiOperation(value="用户推荐搭配详情,状态 0,1 都表示搭配中")
    @GetMapping("/detail")
    public Result detail(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }

        BoxUserRecommend userRecommend = boxUserRecommendService.getOne(new QueryWrapper<BoxUserRecommend>().eq("deleted", 0).eq("user_id", userId));
        if (userRecommend == null) {
            return Result.failure("没有您的推荐搭配信息，您可以答完题在推荐搭配");
        }

        if (userRecommend.getRecommendStatus() == 0 || userRecommend.getRecommendStatus() == 1) {
            //todo 在搭配中，推荐猜你喜欢的衣服
        }

        List<BoxRecommend> boxRecommendList = boxRecommendService.list(new QueryWrapper<BoxRecommend>().eq("user_id", userId).
                eq("deleted", 0).eq("audit_status", 1).orderByDesc("sort"));
        if (boxRecommendList != null && !boxRecommendList.isEmpty()) {
            for (BoxRecommend boxRecommend : boxRecommendList) {
                List<BoxRecommendGoods> recommendGoodsList = boxRecommendGoodsService.list(new QueryWrapper<BoxRecommendGoods>().
                        eq("box_recommend_id", boxRecommend.getId()).eq("deleted", 0));

                boxRecommend.setBoxRecommendGoodsList(recommendGoodsList);
            }
        }

        return Result.success().setData("userRecommend", userRecommend).setData("boxRecommendList", boxRecommendList);
    }

    @ApiOperation(value="搭配单评价")
    @PostMapping("/comment")
    public Result comment (@RequestBody BoxRecommendComment boxRecommendComment) {
        if (boxRecommendComment.getBoxRecommendId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxRecommendComment recommendComment = boxRecommendCommentService.getOne(new QueryWrapper<BoxRecommendComment>().
                eq("deleted", 0).eq("box_recommend_id", boxRecommendComment.getBoxRecommendId()));
        if (recommendComment != null) {
            boxRecommendComment.setId(recommendComment.getId());
            boxRecommendComment.setUpdateTime(System.currentTimeMillis());
            boxRecommendCommentService.updateById(boxRecommendComment);
            return Result.success();
        }

        // 设置搭配单为已评价
        BoxRecommend boxRecommend = new BoxRecommend();
        boxRecommend.setId(boxRecommendComment.getBoxRecommendId());
        boxRecommend.setCommentStatus(1);
        boxRecommend.setUpdateTime(System.currentTimeMillis());
        boxRecommendService.updateById(boxRecommend);

        boxRecommendComment.setUpdateTime(System.currentTimeMillis());
        boxRecommendComment.setCreateTime(System.currentTimeMillis());
        boxRecommendCommentService.save(boxRecommendComment);
        return Result.success();
    }

}

