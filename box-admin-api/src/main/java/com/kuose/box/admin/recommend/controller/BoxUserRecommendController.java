package com.kuose.box.admin.recommend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.recommend.dto.BoxUserRecommendDTO;
import com.kuose.box.admin.recommend.service.BoxRecommendGoodsService;
import com.kuose.box.admin.recommend.service.BoxRecommendService;
import com.kuose.box.admin.recommend.service.BoxUserRecommendService;
import com.kuose.box.admin.user.service.BoxUserService;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.recommend.entity.BoxRecommend;
import com.kuose.box.db.recommend.entity.BoxRecommendGoods;
import com.kuose.box.db.recommend.entity.BoxUserRecommend;
import com.kuose.box.db.user.entity.BoxUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private BoxUserService boxUserService;
    @Autowired
    private BoxRecommendService boxRecommendService;
    @Autowired
    private BoxRecommendGoodsService boxRecommendGoodsService;

    @ApiOperation(value="推荐搭配用户列表")
    @GetMapping("/list")
    public Result list(BoxUserRecommendDTO boxUserRecommendDTO, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Page<BoxUserRecommend> boxUserRecommendPage = new Page<>();
        boxUserRecommendPage.setSize(limit);
        boxUserRecommendPage.setCurrent(page);

        IPage<BoxUserRecommend> boxUserRecommendIPage = boxUserRecommendService.listUserRecommendPage(boxUserRecommendPage, boxUserRecommendDTO);
        List<BoxUserRecommend> records = boxUserRecommendIPage.getRecords();
        for (BoxUserRecommend boxUserRecommend : records) {
            int count = boxRecommendService.count(new QueryWrapper<BoxRecommend>().
                    eq("user_id", boxUserRecommend.getUserId()).eq("deleted", 0).eq("audit_status", 1));
            BoxUser boxUser = boxUserService.getById(boxUserRecommend.getUserId());

            boxUserRecommend.setBoxUser(boxUser);
            boxUserRecommend.setRecommendNumber(count);
        }
        boxUserRecommendIPage.setRecords(records);

        return Result.success().setData("boxUserRecommendIPage", boxUserRecommendIPage);
    }

    @ApiOperation(value="用户推荐搭配详情")
    @GetMapping("/detail")
    public Result detail(Integer userId) {
        if (userId == null) {
            return Result.failure("缺少必传参数");
        }

        List<BoxRecommend> boxRecommendList = boxRecommendService.list(new QueryWrapper<BoxRecommend>().eq("user_id", userId).eq("deleted", 0));
        if (boxRecommendList != null && !boxRecommendList.isEmpty()) {
            for (BoxRecommend boxRecommend : boxRecommendList) {
                List<BoxRecommendGoods> recommendGoodsList = boxRecommendGoodsService.list(new QueryWrapper<BoxRecommendGoods>().
                        eq("box_recommend_id", boxRecommend.getId()).eq("deleted", 0));

                boxRecommend.setBoxRecommendGoodsList(recommendGoodsList);
            }
        }

        return Result.success().setData("boxRecommendList", boxRecommendList);
    }


}

