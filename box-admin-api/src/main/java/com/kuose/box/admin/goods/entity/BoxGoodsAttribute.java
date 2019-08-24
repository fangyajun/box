package com.kuose.box.admin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 商品参数表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-24
 */
@Data
public class BoxGoodsAttribute extends Model<BoxGoodsAttribute> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品表的商品ID
     */
    @ApiModelProperty(value="商品表的商品ID")
    private Integer goodsId;

    /**
     * 商品参数名称
     */
    @ApiModelProperty(value="商品参数名称")
    private String attribute;

    /**
     * 商品参数编码
     */
    @ApiModelProperty(value="商品参数编码")
    private String attributeCode;

    /**
     * 商品参数值
     */
    @ApiModelProperty(value="商品参数值,目前与参数名称一致")
    private String value;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Long addTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(hidden = true)
    private Integer deleted;

}
