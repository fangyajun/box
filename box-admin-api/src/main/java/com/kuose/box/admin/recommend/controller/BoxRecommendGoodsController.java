package com.kuose.box.admin.recommend.controller;


import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.admin.recommend.dto.RecommendGoodsDTO;
import com.kuose.box.admin.recommend.service.BoxRecommendGoodsService;
import com.kuose.box.admin.recommend.service.BoxRecommendService;
import com.kuose.box.common.config.Result;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.goods.entity.BoxGoodsSku;
import com.kuose.box.db.recommend.entity.BoxRecommend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户推荐商品商品表 前端控制器
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Api(tags = {"用户推荐，推荐搭配商品"})
@RestController
@RequestMapping("/boxRecommendGoods")
public class BoxRecommendGoodsController {

    @Autowired
    private BoxRecommendGoodsService boxRecommendGoodsService;
    @Autowired
    private BoxRecommendService boxRecommendService;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;

    @ApiOperation(value="添加或修改商品到搭配单")
    @PostMapping("/add")
    public Result add(@RequestBody RecommendGoodsDTO recommendGoodsDTO) {
        if (recommendGoodsDTO.getBoxRecommendId() == null || recommendGoodsDTO.getSkuIds() == null) {
            return Result.failure("缺少必传参数");
        }

        BoxRecommend boxRecommend = boxRecommendService.getById(recommendGoodsDTO.getBoxRecommendId());
        if (boxRecommend == null) {
            return Result.failure("数据异常,该搭配单不存在");
        }

        Integer[] skuIds = recommendGoodsDTO.getSkuIds();
        for (Integer skuId : skuIds) {
            BoxGoodsSku goodsSku = boxGoodsSkuService.getById(skuId);
            if (goodsSku == null) {
                return Result.failure("数据异常，skuId:"+ skuId +"不存在，查无此sku信息");
            }
        }

        if (boxRecommend.getAuditStatus() == 1) {
            return Result.failure("该搭配单已搭配审核，无法操作！");
        }

        if (!StringUtil.isBlank(recommendGoodsDTO.getCoordinatorMessage())) {
            boxRecommend.setRecommendMessage(recommendGoodsDTO.getCoordinatorMessage());
            boxRecommend.setCoordinator(recommendGoodsDTO.getUsername());
            boxRecommend.setUpdateTime(System.currentTimeMillis());
            boxRecommendService.updateById(boxRecommend);
        }

        return boxRecommendGoodsService.save(recommendGoodsDTO);
    }



}

