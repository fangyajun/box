package com.kuose.box.admin.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.goods.dao.BoxGoodsMapper;
import com.kuose.box.admin.goods.dto.GoodsAllinone;
import com.kuose.box.admin.goods.dto.GoodsQueryParameter;
import com.kuose.box.admin.goods.dto.GoodsSkuVo;
import com.kuose.box.admin.goods.entity.BoxGoods;
import com.kuose.box.admin.goods.entity.BoxGoodsAttribute;
import com.kuose.box.admin.goods.entity.BoxGoodsSku;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeService;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.common.config.Result;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 商品基本信息表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Service
public class BoxGoodsServiceImpl extends ServiceImpl<BoxGoodsMapper, BoxGoods> implements BoxGoodsService {

    @Autowired
    private BoxGoodsMapper boxGoodsMapper;
    @Autowired
    private BoxGoodsAttributeService boxGoodsAttributeService;
    @Autowired
    private BoxGoodsSkuService boxGoodsSkuService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result save(GoodsAllinone goodsAllinone) {
        BoxGoods boxGoods = goodsAllinone.getBoxGoods();
        BoxGoodsAttribute[] boxGoodsAttributes = goodsAllinone.getBoxGoodsAttributes();
        BoxGoodsSku[] boxGoodsSkus = goodsAllinone.getBoxGoodsSkus();

        // 验证商品是否已经存在
        Integer selectCount = boxGoodsMapper.selectCount(new QueryWrapper<BoxGoods>().eq("goods_no", boxGoods.getGoodsNo()).eq("deleted", 0));
        if (selectCount >= 1) {
            return Result.failure("此商品已经存在！");
        }
        boxGoods.setUpdateTime(System.currentTimeMillis());
        boxGoods.setCreateTime(System.currentTimeMillis());
        boxGoodsMapper.insert(boxGoods);

        // 添加商品属性
        if (boxGoodsAttributes != null && boxGoodsAttributes.length != 0) {
            for (BoxGoodsAttribute boxGoodsAttribute : boxGoodsAttributes) {
                boxGoodsAttribute.setGoodsId(boxGoods.getId());
                boxGoodsAttribute.setAddTime(System.currentTimeMillis());
                boxGoodsAttribute.setUpdateTime(System.currentTimeMillis());

                if (boxGoodsAttributeService.count(new QueryWrapper<BoxGoodsAttribute>().eq("goods_id", boxGoods.getId()).eq("attribute_code", boxGoodsAttribute.getAttributeCode()).
                        eq("deleted", 0)) >= 1) {
                    boxGoodsMapper.deleteById(boxGoods.getId());
                    return Result.failure("商品属性编码" + boxGoodsAttribute.getAttributeCode() + "已经存在，请重新添加");
                }

                boxGoodsAttributeService.save(boxGoodsAttribute);
            }
        }

        if (boxGoodsSkus != null && boxGoodsSkus.length != 0) {
            // 添加商品SKU
            for (BoxGoodsSku goodsSkus : boxGoodsSkus) {
                goodsSkus.setGoodsId(boxGoods.getId());
                goodsSkus.setAddTime(System.currentTimeMillis());
                goodsSkus.setUpdateTime(System.currentTimeMillis());

                if (boxGoodsSkuService.count(new QueryWrapper<BoxGoodsSku>().eq("sku_code", goodsSkus.getSkuCode()).
                        eq("deleted", 0)) >= 1) {
                    boxGoodsMapper.deleteById(boxGoods.getId());
                    boxGoodsAttributeService.remove(new QueryWrapper<BoxGoodsAttribute>().eq("goods_id", boxGoods.getId()));
                    return Result.failure("商品SKU编码" + goodsSkus.getSkuCode() + "已经存在，请重新添加");
                }

                boxGoodsSkuService.save(goodsSkus);
            }
        }

        return Result.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteGoods(BoxGoods boxGoods) {
        // 逻辑删除
        boxGoods.setDeleted(1);
        boxGoodsMapper.updateById(boxGoods);

        boxGoodsAttributeService.update(new UpdateWrapper<BoxGoodsAttribute>().eq("goods_id", boxGoods.getId()).set("deleted", 1));
        boxGoodsSkuService.update(new UpdateWrapper<BoxGoodsSku>().eq("goods_id", boxGoods.getId()).set("deleted", 1));
    }

    @Override
    public IPage<BoxGoods> listGoodsPage(Page<BoxGoods> boxGoodsPage, GoodsQueryParameter goodsQueryParameter) {
        return boxGoodsMapper.listGoodsPage(boxGoodsPage, goodsQueryParameter.getCategoryCode(), goodsQueryParameter.getGoodsNo(), goodsQueryParameter.getGoodsName(),
                goodsQueryParameter.getQuarter(), goodsQueryParameter.getYear(), goodsQueryParameter.getLowPrice(), goodsQueryParameter.getHighPrice(), goodsQueryParameter.getGoodsAttributeCodes());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result update(GoodsAllinone goodsAllinone) {
        BoxGoods boxGoods = goodsAllinone.getBoxGoods();
        BoxGoodsAttribute[] boxGoodsAttributes = goodsAllinone.getBoxGoodsAttributes();
        BoxGoodsSku[] boxGoodsSkus = goodsAllinone.getBoxGoodsSkus();

        boxGoods.setUpdateTime(System.currentTimeMillis());
        boxGoodsMapper.updateById(boxGoods);


        if (boxGoodsAttributes != null && boxGoodsAttributes.length != 0) {
            boxGoodsAttributeService.remove(new QueryWrapper<BoxGoodsAttribute>().eq("goods_id", boxGoods.getId()));
            for (BoxGoodsAttribute boxGoodsAttribute : boxGoodsAttributes) {
                boxGoodsAttribute.setGoodsId(boxGoods.getId());
                boxGoodsAttribute.setAddTime(System.currentTimeMillis());
                boxGoodsAttribute.setUpdateTime(System.currentTimeMillis());

                boxGoodsAttributeService.save(boxGoodsAttribute);
            }
        }

        if (boxGoodsSkus != null && boxGoodsSkus.length != 0) {
            // 先删除在添加
            boxGoodsSkuService.remove(new QueryWrapper<BoxGoodsSku>().eq("goods_id", boxGoods.getId()));
            // 添加商品SKU
            for (BoxGoodsSku goodsSkus : boxGoodsSkus) {
                goodsSkus.setGoodsId(boxGoods.getId());
                goodsSkus.setAddTime(System.currentTimeMillis());
                goodsSkus.setUpdateTime(System.currentTimeMillis());

                boxGoodsSkuService.save(goodsSkus);
            }
        }

        return Result.success();
    }

    @Override
    public IPage<GoodsSkuVo> listGoodsAndSku(Page<BoxGoods> boxGoodsPage, GoodsQueryParameter goodsQueryParameter) {
        return boxGoodsMapper.listGoodsAndSku(boxGoodsPage, goodsQueryParameter.getCategoryCode(), goodsQueryParameter.getGoodsNo(), goodsQueryParameter.getGoodsName(),
                goodsQueryParameter.getQuarter(), goodsQueryParameter.getYear(), goodsQueryParameter.getLowPrice(), goodsQueryParameter.getHighPrice(), goodsQueryParameter.getGoodsAttributeCodes());
    }
}

