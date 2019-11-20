package com.kuose.box.wx.recommend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.recommend.entity.*;
import com.kuose.box.wx.annotation.LoginUser;
import com.kuose.box.wx.recommend.service.*;
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
    @Autowired
    private BoxRecommendGoodsCommentService boxRecommendGoodsCommentService;
    @Autowired
    private BoxUserRecommendCommentService boxUserRecommendCommentService;

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
                eq("deleted", 0).eq("user_id", boxUserRecommend.getUserId()).in("recommend_status", 0,1));

        if (userRecommend != null) {
            return Result.failure("您的搭配方案正在搭配中，请您耐心等候！");
        }

        // 没有搭配，新增
        boxUserRecommend.setRecommendStatus(0);
        boxUserRecommend.setCreateTime(System.currentTimeMillis());
        boxUserRecommend.setUpdateTime(System.currentTimeMillis());

        boxUserRecommendService.save(boxUserRecommend);
        return Result.success();
    }

    @ApiOperation(value="获取搭配中的搭配 或 已经搭配但好没有评价的搭配, (0,1 都表示搭配中,2-表示已搭配但未评价)")
    @GetMapping("/detail")
    public Result detail(Integer userId) {

        if (userId == null) {
            return Result.failure("缺少必传参数");
        }
        BoxUserRecommend userRecommend = boxUserRecommendService.getOne(new QueryWrapper<BoxUserRecommend>().
                eq("deleted", 0).eq("user_id", userId).in("recommend_status", 0,1,2));

        List<BoxRecommend> boxRecommendList = null;
        if (userRecommend != null && userRecommend.getRecommendStatus() == 2) {
            boxRecommendList = boxRecommendService.list(new QueryWrapper<BoxRecommend>().eq("user_recommend_id", userRecommend.getId()).
                    eq("deleted", 0).eq("audit_status", 1).orderByDesc("sort"));
            if (boxRecommendList != null && !boxRecommendList.isEmpty()) {
                for (BoxRecommend boxRecommend : boxRecommendList) {
                    List<BoxRecommendGoods> recommendGoodsList = boxRecommendGoodsService.list(new QueryWrapper<BoxRecommendGoods>().
                            eq("box_recommend_id", boxRecommend.getId()).eq("deleted", 0));

                    boxRecommend.setBoxRecommendGoodsList(recommendGoodsList);
                }
            }
        }
        return Result.success().setData("userRecommend", userRecommend).setData("boxRecommendList", boxRecommendList);
    }



    @ApiOperation(value="获取用户的历史搭配单")
    @GetMapping("/list")
    public Result list(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }
        List<BoxUserRecommend> userRecommendList = boxUserRecommendService.list(new QueryWrapper<BoxUserRecommend>().
                eq("deleted", 0).eq("user_id", userId).eq("recommend_status", 3).orderByDesc("create_time"));

        if (userRecommendList != null && !userRecommendList.isEmpty()) {
            for (BoxUserRecommend boxUserRecommend : userRecommendList) {
                List<BoxRecommend> boxRecommendList = boxRecommendService.list(new QueryWrapper<BoxRecommend>().eq("user_recommend_id", boxUserRecommend.getId()).
                        eq("deleted", 0).eq("audit_status", 1).orderByDesc("sort"));
                if (boxRecommendList != null && !boxRecommendList.isEmpty()) {
                    for (BoxRecommend boxRecommend : boxRecommendList) {
                        List<BoxRecommendGoods> recommendGoodsList = boxRecommendGoodsService.list(new QueryWrapper<BoxRecommendGoods>().
                                eq("box_recommend_id", boxRecommend.getId()).eq("deleted", 0));

                        boxRecommend.setBoxRecommendGoodsList(recommendGoodsList);
                    }
                }
                boxUserRecommend.setBoxRecommendList(boxRecommendList);
            }
        }
        return Result.success().setData("userRecommendList", userRecommendList);
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

    @ApiOperation(value="对一次搭配评价")
    @PostMapping("/recommendComment")
    public Result recommendComment (@RequestBody BoxUserRecommendComment boxUserRecommendComment) {
        if (boxUserRecommendComment.getBoxUserRecommendId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxUserRecommendComment selectUserRecommendComment = boxUserRecommendCommentService.getOne(new QueryWrapper<BoxUserRecommendComment>().
                eq("deleted", 0).eq("box_user_recommend_id", boxUserRecommendComment.getBoxUserRecommendId()));

        if (selectUserRecommendComment != null) {
            boxUserRecommendComment.setId(selectUserRecommendComment.getId());
            boxUserRecommendComment.setUpdateTime(System.currentTimeMillis());
            boxUserRecommendCommentService.updateById(boxUserRecommendComment);
            return Result.success();
        }

        // 改变搭配状态为已完成
        BoxUserRecommend userRecommend = boxUserRecommendService.getById(boxUserRecommendComment.getBoxUserRecommendId());
        userRecommend.setRecommendStatus(3);
        userRecommend.setUpdateTime(System.currentTimeMillis());
        boxUserRecommendService.updateById(userRecommend);

        boxUserRecommendComment.setUpdateTime(System.currentTimeMillis());
        boxUserRecommendComment.setCreateTime(System.currentTimeMillis());
        boxUserRecommendCommentService.save(boxUserRecommendComment);
        return Result.success();
    }


    @ApiOperation(value="推荐商品评价")
    @PostMapping("/recommentGoodsComment")
    public Result recommentGoodsComment(@RequestBody BoxRecommendGoodsComment boxRecommendGoodsComment) {
        if (boxRecommendGoodsComment.getUserId() == null || boxRecommendGoodsComment.getBoxRecommendGoodsId() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxRecommendGoodsComment selectRecommendGoodsComment = boxRecommendGoodsCommentService.getOne(new QueryWrapper<BoxRecommendGoodsComment>().
                eq("deleted", 0).eq("box_recommend_goods_id", boxRecommendGoodsComment.getBoxRecommendGoodsId()));

        BoxRecommendGoods recommendGoods = boxRecommendGoodsService.getById(boxRecommendGoodsComment.getBoxRecommendGoodsId());
        if (recommendGoods == null) {
            return Result.failure("数据异常，未能查到推荐单商品");
        }

        if (selectRecommendGoodsComment == null) {
            BoxRecommendGoodsComment recommendGoodsComment = new BoxRecommendGoodsComment();

            recommendGoodsComment.setSkuId(recommendGoods.getSkuId());
            recommendGoodsComment.setUserId(boxRecommendGoodsComment.getUserId());
            recommendGoodsComment.setSize(boxRecommendGoodsComment.getSize());
            recommendGoodsComment.setStyle(boxRecommendGoodsComment.getStyle());
            recommendGoodsComment.setMatch(boxRecommendGoodsComment.getMatch());
            recommendGoodsComment.setQuality(boxRecommendGoodsComment.getQuality());
            recommendGoodsComment.setPrice(boxRecommendGoodsComment.getPrice());
            recommendGoodsComment.setContent(boxRecommendGoodsComment.getContent());
            recommendGoodsComment.setStar(boxRecommendGoodsComment.getStar());
            recommendGoodsComment.setAddTime(System.currentTimeMillis());
            recommendGoodsComment.setUpdateTime(System.currentTimeMillis());

            boxRecommendGoodsCommentService.save(recommendGoodsComment);
        } else {
            selectRecommendGoodsComment.setSize(boxRecommendGoodsComment.getSize());
            selectRecommendGoodsComment.setStyle(boxRecommendGoodsComment.getStyle());
            selectRecommendGoodsComment.setMatch(boxRecommendGoodsComment.getMatch());
            selectRecommendGoodsComment.setQuality(boxRecommendGoodsComment.getQuality());
            selectRecommendGoodsComment.setPrice(boxRecommendGoodsComment.getPrice());
            selectRecommendGoodsComment.setContent(boxRecommendGoodsComment.getContent());
            selectRecommendGoodsComment.setStar(boxRecommendGoodsComment.getStar());
            selectRecommendGoodsComment.setUpdateTime(System.currentTimeMillis());

            boxRecommendGoodsCommentService.updateById(selectRecommendGoodsComment);
        }

        return Result.success();
    }

}

