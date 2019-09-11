package com.kuose.source.goods.service.impl;

import com.kuose.source.goods.entity.AttributeSource;
import com.kuose.source.goods.entity.BoxGoods;
import com.kuose.source.goods.entity.RipreportProductinformation;
import com.kuose.source.goods.dao.RipreportProductinformationMapper;
import com.kuose.source.goods.service.RipreportProductinformationService;
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
public class RipreportProductinformationServiceImpl extends ServiceImpl<RipreportProductinformationMapper, RipreportProductinformation> implements RipreportProductinformationService {

    @Autowired
    private RipreportProductinformationMapper productinformationMapper;

    @Override
    public List<BoxGoods> listGoods(String productno, String goodsName, String year) {
        return productinformationMapper.listGoods(productno, goodsName, year);
    }

    @Override
    public BoxGoods getGoods(String productno) {
        return productinformationMapper.getGoods(productno);
    }

    @Override
    public List<AttributeSource> listAllAttibutes() {
        return productinformationMapper.listAllAttibutes();
    }

    @Override
    public List<AttributeSource>getGoodsAttibuteByGoodsId(Integer id) {
        return productinformationMapper.getGoodsAttibuteByGoodsId(id);
    }
}
