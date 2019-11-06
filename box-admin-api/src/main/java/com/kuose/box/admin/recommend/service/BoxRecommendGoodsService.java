package com.kuose.box.admin.recommend.service;

import com.kuose.box.admin.recommend.dto.RecommendGoodsDTO;
import com.kuose.box.common.config.Result;
import com.kuose.box.db.recommend.entity.BoxRecommendGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户推荐商品商品表 服务类
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
public interface BoxRecommendGoodsService extends IService<BoxRecommendGoods> {

    Result save(RecommendGoodsDTO recommendGoodsDTO);
}
