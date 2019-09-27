package com.kuose.box.admin.match.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.goods.service.BoxGoodsService;
import com.kuose.box.admin.match.dto.GoodsMatchParameter;
import com.kuose.box.common.utils.StringUtil;
import com.kuose.box.db.goods.dto.GoodsSkuVo;
import com.kuose.box.db.goods.entity.BoxGoods;
import com.kuose.box.db.user.entity.BoxUserBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/10
 */
@Service
public class GoodsMatchService {
    @Autowired
    private BoxGoodsService boxGoodsService;

    public IPage<GoodsSkuVo> listMatchGoods(Page<BoxGoods> boxGoodsPage, GoodsMatchParameter goodsMatchParameter, BoxUserBase userBase) {
        String topSize = userBase.getTopSize();
        String dressSize = userBase.getDressSize();
        String bottomsSize = userBase.getBottomsSize();
        String jeansSize = userBase.getJeansSize();
        String[] avoidColor = userBase.getAvoidColor();
        String[] avoidTexture = userBase.getAvoidTexture();
        String[] avoidCategory = userBase.getAvoidCategory();
        String[] avoidFigure = userBase.getAvoidFigure();

        Double topLowPrice = null;
        Double topHighPrice = null;
        String topPrice = userBase.getTopPrice();
        if (!StringUtil.isBlank(topPrice) && !"-1".equals(topPrice)) {
            String[] top = topPrice.split("-");
            topLowPrice = Double.parseDouble(top[0]);
            topHighPrice = Double.parseDouble(top[1]);
        }

        Double bottomsLowPrice = null;
        Double bottomsHighPrice = null;
        String bottomsPrice = userBase.getBottomsPrice();
        if (!StringUtil.isBlank(bottomsPrice) && !"-1".equals(bottomsPrice)) {
            String[] bottoms = bottomsPrice.split("-");
            bottomsLowPrice = Double.parseDouble(bottoms[0]);
            bottomsHighPrice = Double.parseDouble(bottoms[1]);
        }

        Double dressLowPrice = null;
        Double dressHighPrice = null;
        String dressPrice = userBase.getDressPrice();
        if (!StringUtil.isBlank(dressPrice) && !"-1".equals(dressPrice)) {
            String[] dress = dressPrice.split("-");
            dressLowPrice = Double.parseDouble(dress[0]);
            dressHighPrice = Double.parseDouble(dress[1]);
        }

        Double overcoatLowPrice = null;
        Double overcoatHighPrice = null;
        String overcoatPrice = userBase.getOvercoatPrice();
        if (!StringUtil.isBlank(overcoatPrice) && !"-1".equals(overcoatPrice)) {
            String[] overcoat = overcoatPrice.split("-");
            overcoatLowPrice = Double.parseDouble(overcoat[0]);
            overcoatHighPrice = Double.parseDouble(overcoat[1]);
        }

        goodsMatchParameter.setTopSize(topSize);
        goodsMatchParameter.setBottomsSize(bottomsSize);
        goodsMatchParameter.setDressSize(dressSize);
        goodsMatchParameter.setJeansSize(jeansSize);
        goodsMatchParameter.setTopLowPrice(topLowPrice);
        goodsMatchParameter.setTopHighPrice(topHighPrice);
        goodsMatchParameter.setDressLowPrice(dressLowPrice);
        goodsMatchParameter.setDressHighPrice(dressHighPrice);
        goodsMatchParameter.setBottomsLowPrice(bottomsLowPrice);
        goodsMatchParameter.setBottomsHighPrice(bottomsHighPrice);
        goodsMatchParameter.setOvercoatLowPrice(overcoatLowPrice);
        goodsMatchParameter.setOvercoatHighPrice(overcoatHighPrice);
        goodsMatchParameter.setAvoidCategory(avoidCategory);
        goodsMatchParameter.setAvoidColor(avoidColor);
        goodsMatchParameter.setAvoidFigure(avoidFigure);
        goodsMatchParameter.setAvoidTexture(avoidTexture);

        IPage<GoodsSkuVo> goodsSkuVoIPage = boxGoodsService.listMatchGoods(boxGoodsPage, goodsMatchParameter);
        return goodsSkuVoIPage;
    }
}
