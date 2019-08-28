package com.kuose.source.goods.service.impl;

import com.kuose.source.goods.entity.BoxGoodsSku;
import com.kuose.source.goods.entity.RipreportProductinformationsku;
import com.kuose.source.goods.dao.RipreportProductinformationskuMapper;
import com.kuose.source.goods.service.RipreportProductinformationskuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
@Service
public class RipreportProductinformationskuServiceImpl extends ServiceImpl<RipreportProductinformationskuMapper, RipreportProductinformationsku> implements RipreportProductinformationskuService {

    @Autowired
    private RipreportProductinformationskuMapper ripreportProductinformationskuMapper;

    @Override
    public List<BoxGoodsSku> listGoodsSku(String goodsNo) {
        return ripreportProductinformationskuMapper.listGoodsSku(goodsNo);
    }
}
