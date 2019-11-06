package com.kuose.box.db.recommend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.db.recommend.entity.BoxUserRecommend;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户推荐表 Mapper 接口
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
public interface BoxUserRecommendMapper extends BaseMapper<BoxUserRecommend> {

    IPage<BoxUserRecommend> listUserRecommendPage(Page<BoxUserRecommend> boxUserRecommendPage, @Param("recommendStatus") Integer recommendStatus);
}
