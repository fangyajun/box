package com.kuose.box.admin.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeLabelService;
import com.kuose.box.db.goods.dao.BoxGoodsAttributeLabelMapper;
import com.kuose.box.db.goods.entity.BoxGoodsAttributeLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品属性标签表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-23
 */
@Service
public class BoxGoodsAttributeLabelServiceImpl extends ServiceImpl<BoxGoodsAttributeLabelMapper, BoxGoodsAttributeLabel> implements BoxGoodsAttributeLabelService {

    @Autowired
    private  BoxGoodsAttributeLabelMapper boxGoodsAttributeLabelMapper;

    @Override
    public List<BoxGoodsAttributeLabel> listGoodsAttributeLabel(String type,String nodeCategory) {
        return boxGoodsAttributeLabelMapper.listGoodsAttributeLabel(type, nodeCategory);
    }

    @Override
    public List<BoxGoodsAttributeLabel> listNodeCategory() {
        return boxGoodsAttributeLabelMapper.listNodeCategory();
    }
}
