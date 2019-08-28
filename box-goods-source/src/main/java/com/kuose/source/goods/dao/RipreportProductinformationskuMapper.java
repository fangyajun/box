package com.kuose.source.goods.dao;

import com.kuose.source.goods.entity.BoxGoodsSku;
import com.kuose.source.goods.entity.RipreportProductinformationsku;
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
public interface RipreportProductinformationskuMapper extends BaseMapper<RipreportProductinformationsku> {

    List<BoxGoodsSku> listGoodsSku(@Param("productno") String goodsNo);
}
