package com.kuose.box.admin.goods.dto;

import com.kuose.box.admin.goods.entity.BoxGoods;
import com.kuose.box.admin.goods.entity.BoxGoodsAttribute;
import com.kuose.box.admin.goods.entity.BoxGoodsSku;
import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/8/24
 */
@Data
public class GoodsAllinone {
    private BoxGoods boxGoods;
    private BoxGoodsAttribute[] boxGoodsAttributes;
    private BoxGoodsSku[] boxGoodsSkus;
}
