package com.kuose.source.goods.dao;

import com.kuose.source.goods.entity.BoxGoods;
import com.kuose.source.goods.entity.RipreportProductinformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
public interface RipreportProductinformationMapper extends BaseMapper<RipreportProductinformation> {

    List<BoxGoods> listGoods(@Param("productno") String productno, @Param("goodsName") String goodsName);

    BoxGoods getGoods(@Param("productno") String productno);
}
