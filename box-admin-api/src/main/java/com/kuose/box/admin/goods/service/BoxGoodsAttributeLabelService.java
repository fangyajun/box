package com.kuose.box.admin.goods.service;

import com.kuose.box.admin.goods.entity.BoxGoodsAttributeLabel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性标签表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-23
 */
public interface BoxGoodsAttributeLabelService extends IService<BoxGoodsAttributeLabel> {

    List<BoxGoodsAttributeLabel> listGoodsAttributeLabel(String type);
}
