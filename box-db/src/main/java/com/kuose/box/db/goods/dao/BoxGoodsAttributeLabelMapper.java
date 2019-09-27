package com.kuose.box.db.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuose.box.db.goods.entity.BoxGoodsAttributeLabel;
import org.apache.ibatis.annotations.Param;

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

    List<BoxGoodsAttributeLabel> listGoodsAttributeLabel(@Param("type") String type, @Param("nodeCategory") String nodeCategory);

    List<BoxGoodsAttributeLabel> listNodeCategory();
}
