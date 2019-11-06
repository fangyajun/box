package com.kuose.box.admin.recommend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.recommend.dto.BoxUserRecommendDTO;
import com.kuose.box.admin.recommend.service.BoxUserRecommendService;
import com.kuose.box.db.recommend.dao.BoxUserRecommendMapper;
import com.kuose.box.db.recommend.entity.BoxUserRecommend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户推荐表 服务实现类
 * </p>
 *
 * @author 魔舞清华
 * @since 2019-11-06
 */
@Service
public class BoxUserRecommendServiceImpl extends ServiceImpl<BoxUserRecommendMapper, BoxUserRecommend> implements BoxUserRecommendService {

    @Autowired
    private BoxUserRecommendMapper boxUserRecommendMapper;

    @Override
    public IPage<BoxUserRecommend> listUserRecommendPage(Page<BoxUserRecommend> boxUserRecommendPage, BoxUserRecommendDTO boxUserRecommendDTO) {
        return boxUserRecommendMapper.listUserRecommendPage(boxUserRecommendPage, boxUserRecommendDTO.getRecommendStatus());
    }
}
