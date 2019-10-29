package com.kuose.box.db.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.db.order.entity.BoxOrder;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-05
 */
public interface BoxOrderMapper extends BaseMapper<BoxOrder> {

    IPage<BoxOrder> listOrderPage(Page<BoxOrder> boxOrderPage, @Param("orderNo") String orderNo, @Param("minExpectTime") Long minExpectTime, @Param("maxExpectTime") Long maxExpectTime,
                                  @Param("mobile") String mobile, @Param("orderStatus") Integer orderStatus, @Param("auditStatus") Integer auditStatus);
}
