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
import com.kuose.box.admin.goods.entity.BoxGoodsCategory;
import com.kuose.box.admin.goods.entity.BoxGoodsSku;
import com.kuose.box.admin.goods.service.BoxGoodsAttributeService;
import com.kuose.box.admin.goods.service.BoxGoodsCategoryService;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.goods.service.BoxGoodsSkuService;
import com.kuose.box.admin.match.dto.GoodsMatchParameter;
import com.kuose.box.common.config.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private BoxGoodsCategoryService boxGoodsCategoryService;

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
        String[] categoryCodes = null;
        // 如果传进来是大类别，就找出大类别下的子类别并加入数组
        if ("SZ".equals(goodsQueryParameter.getCategoryCode()) || "QZ".equals(goodsQueryParameter.getCategoryCode()) ||
                "XZ".equals(goodsQueryParameter.getCategoryCode()) || "PS".equals(goodsQueryParameter.getCategoryCode())) {
            BoxGoodsCategory boxGoodsCategory = boxGoodsCategoryService.getOne(new QueryWrapper<BoxGoodsCategory>().eq("category_code", goodsQueryParameter.getCategoryCode()));
            List<BoxGoodsCategory> goodsCategories = boxGoodsCategoryService.list(new QueryWrapper<BoxGoodsCategory>().eq("parent_id", boxGoodsCategory.getId()).eq("deleted", 0));
            if (goodsCategories != null && goodsCategories.size() >= 1) {
                // 数组初始化
                categoryCodes = new String[goodsCategories.size()];
                int i = 0;
                for (BoxGoodsCategory goodsCategory : goodsCategories) {
                    categoryCodes[i++] =   goodsCategory.getCategoryCode();
                }
            }
        } else {
            if (goodsQueryParameter.getCategoryCode() != null) {
                categoryCodes = new String[1];
                categoryCodes [0] = goodsQueryParameter.getCategoryCode();
            }
        }

        return boxGoodsMapper.listGoodsAndSku(boxGoodsPage, categoryCodes, goodsQueryParameter.getGoodsNo(), goodsQueryParameter.getGoodsName(),
                goodsQueryParameter.getQuarter(), goodsQueryParameter.getYear(), goodsQueryParameter.getLowPrice(), goodsQueryParameter.getHighPrice(), goodsQueryParameter.getGoodsAttributeCodes(),
                goodsQueryParameter.getColorName(), goodsQueryParameter.getColorCode(), goodsQueryParameter.getSizeCode());
    }

    @Override
    public IPage<GoodsSkuVo> listMatchGoods(Page<BoxGoods> boxGoodsPage, GoodsMatchParameter goodsMatchParameter) {
        String[] categoryCodes = null;
        // 如果传进来是大类别，就找出大类别下的子类别并加入数组
        if ("SZ".equals(goodsMatchParameter.getCategoryCode()) || "QZ".equals(goodsMatchParameter.getCategoryCode()) ||
                "XZ".equals(goodsMatchParameter.getCategoryCode()) || "PS".equals(goodsMatchParameter.getCategoryCode())) {
            BoxGoodsCategory boxGoodsCategory = boxGoodsCategoryService.getOne(new QueryWrapper<BoxGoodsCategory>().eq("category_code", goodsMatchParameter.getCategoryCode()));
            List<BoxGoodsCategory> goodsCategories = boxGoodsCategoryService.list(new QueryWrapper<BoxGoodsCategory>().eq("parent_id", boxGoodsCategory.getId()).eq("deleted", 0));
            if (goodsCategories != null && goodsCategories.size() >= 1) {
                // 数组初始化
                categoryCodes = new String[goodsCategories.size()];
                int i = 0;
                for (BoxGoodsCategory goodsCategory : goodsCategories) {
                    categoryCodes[i++] =   goodsCategory.getCategoryCode();
                }
            }
        } else {
            if (goodsMatchParameter.getCategoryCode() != null) {
                categoryCodes = new String[1];
                categoryCodes [0] = goodsMatchParameter.getCategoryCode();
            }
        }

        return boxGoodsMapper.listMatchGoods(boxGoodsPage, categoryCodes, goodsMatchParameter.getGoodsNo(), goodsMatchParameter.getGoodsName(),
                goodsMatchParameter.getQuarter(), goodsMatchParameter.getYear(), goodsMatchParameter.getLowPrice(), goodsMatchParameter.getHighPrice(),
                goodsMatchParameter.getGoodsAttributeCodes(), goodsMatchParameter.getColorName(), goodsMatchParameter.getColorCode(), goodsMatchParameter.getSizeCode(),
                goodsMatchParameter.getAvoidColor(), goodsMatchParameter.getAvoidTexture(), goodsMatchParameter.getAvoidCategory(), goodsMatchParameter.getAvoidFigure(),
                goodsMatchParameter.getTopSize(), goodsMatchParameter.getDressSize(), goodsMatchParameter.getBottomsSize(), goodsMatchParameter.getJeansSize(),
                goodsMatchParameter.getTopLowPrice(),goodsMatchParameter.getTopHighPrice(),goodsMatchParameter.getBottomsLowPrice(),goodsMatchParameter.getBottomsHighPrice(),
                goodsMatchParameter.getDressLowPrice(),goodsMatchParameter.getDressHighPrice(),goodsMatchParameter.getOvercoatLowPrice(),goodsMatchParameter.getOvercoatHighPrice());
    }
}

