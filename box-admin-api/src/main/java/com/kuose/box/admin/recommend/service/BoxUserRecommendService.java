package com.kuose.box.admin.recommend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.recommend.dto.BoxUserRecommendDTO;
import com.kuose.box.db.recommend.entity.BoxUserRecommend;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户推荐表 服务类
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
public interface BoxUserRecommendService extends IService<BoxUserRecommend> {

    IPage<BoxUserRecommend> listUserRecommendPage(Page<BoxUserRecommend> boxUserRecommendPage, BoxUserRecommendDTO boxUserRecommendDTO);
}
