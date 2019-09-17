package com.kuose.box.admin.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.goods.dto.GoodsSkuVo;
import com.kuose.box.admin.goods.entity.BoxGoods;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品基本信息表 Mapper 接口
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
public interface BoxGoodsMapper extends BaseMapper<BoxGoods> {

    IPage<BoxGoods> listGoodsPage(Page<BoxGoods> boxGoodsPage, @Param("categoryCode") String categoryCode, @Param("goodsNo") String goodsNo,
                                  @Param("goodsName") String goodsName,@Param("quarter") String quarter, @Param("year") Integer year, @Param("lowPrice") Double lowPrice,
                                  @Param("highPrice") Double highPrice, @Param("goodsAttributeCodes") String[] goodsAttributeCodes);

    IPage<GoodsSkuVo> listGoodsAndSku(Page<BoxGoods> boxGoodsPage, @Param("categoryCode") String[] categoryCodes, @Param("goodsNo") String goodsNo,
                                      @Param("goodsName") String goodsName,@Param("quarter") String quarter, @Param("year") Integer year, @Param("lowPrice") Double lowPrice,
                                      @Param("highPrice") Double highPrice, @Param("goodsAttributeCodes") String[] goodsAttributeCodes,
                                      @Param("colorName") String colorName, @Param("colorCode") String colorCode, @Param("sizeCode") String sizeCode);

    IPage<GoodsSkuVo> listMatchGoods(Page<BoxGoods> boxGoodsPage, @Param("categoryCode")String[] categoryCode, @Param("goodsNo")String goodsNo, @Param("goodsName")String goodsName,
                                     @Param("quarter")String quarter, @Param("year")Integer year, @Param("lowPrice")Double lowPrice, @Param("highPrice")Double highPrice,
                                     @Param("goodsAttributeCodes")String[] goodsAttributeCodes, @Param("colorName")String colorName, @Param("colorCode")String colorCode,
                                     @Param("sizeCode")String sizeCode, @Param("avoidColor")String[] avoidColor, @Param("avoidTexture")String[] avoidTexture,
                                     @Param("avoidCategory")String[] avoidCategory,@Param("avoidFigure") String[] avoidFigure, @Param("topSize")String topSize,
                                     @Param("dressSize")String dressSize, @Param("bottomsSize")String bottomsSize, @Param("jeansSize")String jeansSize,
                                     @Param("topLowPrice")Double topLowPrice, @Param("topHighPrice")Double topHighPrice, @Param("bottomsLowPrice")Double bottomsLowPrice,
                                     @Param("bottomsHighPrice")Double bottomsHighPrice, @Param("dressLowPrice")Double dressLowPrice, @Param("dressHighPrice")Double dressHighPrice,
                                     @Param("overcoatLowPrice")Double overcoatLowPrice, @Param("overcoatHighPrice")Double overcoatHighPrice);
}
