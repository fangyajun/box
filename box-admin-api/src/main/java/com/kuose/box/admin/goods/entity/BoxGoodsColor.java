package com.kuose.box.admin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 颜色表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-27
 */
@Getter
@Setter
public class BoxGoodsColor extends Model<BoxGoodsColor> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 颜色名称
     */
    @ApiModelProperty(example = "白色")
    private String colorName;

    /**
     * 颜色编码
     */
    @ApiModelProperty(example = "111")
    private String colorCode;

    /**
     * 色系
     */
    @ApiModelProperty(example = "白色系")
    private String aliasName;

    /**
     * 状态,0:正常状态
     */
    @ApiModelProperty(hidden = true)
    private Integer colorStatus;

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
