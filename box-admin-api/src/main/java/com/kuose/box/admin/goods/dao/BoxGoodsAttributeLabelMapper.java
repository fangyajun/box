package com.kuose.box.admin.goods.dao;

import com.kuose.box.admin.goods.entity.BoxGoodsAttributeLabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品属性标签表 Mapper 接口
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-23
 */
public interface BoxGoodsAttributeLabelMapper extends BaseMapper<BoxGoodsAttributeLabel> {

    List<BoxGoodsAttributeLabel> listGoodsAttributeLabel(String type);
}
